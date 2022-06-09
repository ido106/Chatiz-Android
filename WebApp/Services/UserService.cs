using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;
using Microsoft.EntityFrameworkCore;
using Repository;

namespace Services
{
    public class UserService : IUserService
    {
        private readonly WebAppContext _context;

        public UserService(WebAppContext context)
        {
            _context = context;
        }


        public async Task<List<User>> GetAll()
        {
            return await _context.User.ToListAsync();
        }

        public async Task<User> Get(string username)
        {
            if (username == null) return null;
            return  await _context.User.Include(x => x.Contacts).FirstOrDefaultAsync(u => u.Username.Equals(username));
        }

        public async Task Add(User user)
        {
            if (user == null) return;
            if (user.Contacts == null) user.Contacts = new List<Contact>();
            await _context.User.AddAsync(user);
            await _context.SaveChangesAsync();
        }

        public async Task<bool> Exist(string username)
        {
           if (username == null) return false;
           var q = await _context.User.FirstOrDefaultAsync(x => x.Username.Equals(username));
           return q != null;
        }

        public async Task<List<Contact>> GetContacts(string username)
        {
            return await _context.Contact.Include(c => c.Messages).Where(c => c.TalkingTo.Equals(username)).ToListAsync();
  
        }

        public async Task<List<Message>> GetMessages(string from, string to)
        {
            if(from == null || to == null) return null;

            return await _context.Message.Where(message => (message.to == to && message.from == from))
                .OrderBy(message => message.Id).ToListAsync();
        }

        public async Task<Contact> GetContact(string username, string contact_name)
        {
            if (username == null || contact_name == null) return null;

            List<Contact> all_contacts = await GetContacts(username);
            if (all_contacts == null) return null;
            Contact contact = all_contacts.FirstOrDefault(x => x.Id == contact_name);
            return contact;
        }

        public async Task<bool> AddContact(string username, string contact_name, string contact_nickname, string contact_server)
        {
            if (username == null || contact_name == null || contact_nickname == null || contact_server == null) return false;

            User user = await Get(username);
            if(user == null) return false;


            Contact c = await GetContact(username, contact_name);
            if (c != null) return false;

            if (await GetContacts(username) == null) user.Contacts = new List<Contact>();

            Contact contact = new();
            contact.TalkingTo = username;
            contact.Id = contact_name;
            contact.Nickname = contact_nickname;
            contact.Server = contact_server;
            contact.LastSeen = DateTime.Now;
            contact.Messages = new List<Message>();

            user.Contacts.Add(contact);
            _context.Update(user);
            
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteContact(string username, string contact_name)
        {
            if(username==null || contact_name == null) return false;

            User user = await Get(username);
            if(user == null || username.Equals(contact_name)) return false;
            Contact contact = await GetContact(username, contact_name);
            if(contact == null) return false;

            user.Contacts.Remove(contact);
            _context.Update(user);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> ChangeContact(string username, string contact_username, string nickname, string server)
        {
            if (username==null || contact_username == null || nickname == null || server == null) return false;

            Contact c = await GetContact(username, contact_username);
            if(c == null) return false;

            c.Server = server;
            c.Nickname = nickname;
            _context.Update(c);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<Message> GetLast(string username, string contact)
        {
            if (username == null || contact == null) return null;

            Contact c = await GetContact(username, contact);
            if (await Get(username) == null ||  c == null || c.Messages.Count == 0)
            {
                return null;
            }

            //Contact c = (await Get(username)).Contacts.Find(x => x.ContactUsername == contact);
            return c.Messages.Last();
        }

        public async Task<Message> GetMessageID(string username, string contact, int id)
        {
            if (username == null || contact == null || 
                await Get(username) == null || await GetContact(username, contact) == null)
            {
                return null;
            }
            return (await GetMessages(username, contact)).FirstOrDefault(m => m.Id == id);
        }

        public async Task<bool> AddMessage(string from, string to, string content, bool sent)
        {
            if(from == null || to == null || content== null || content.Length == 0) return false;

            if (await Get(from) == null) return false;
            Contact c = await GetContact(from, to);
            if (c == null) return false;


            Message message = new();
            message.from = from;
            message.to = to;
            message.Sent = sent;
            int id = (await GetLast(from, to)).Id + 1;
            message.Id = id;
            message.Content = content;
            message.TimeSent = DateTime.Now;

            c.Messages.Add(message);
            c.LastMessage = content;

            //_context.Add(message);
            _context.Update(c);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> UpdateMessage(string contact_username, int id, string username, string newData)
        {
            if (contact_username == null || username == null || newData == null || newData.Length == 0) return false;

            Contact c = await GetContact(username, contact_username);
            if (c == null) return false;

            Message m = c.Messages.FirstOrDefault(m => m.Id == id);
            if (m == null) return false;
            m.Content = newData;
            _context.Update(m);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteMessage(string username, string contact, int id)
        {
            if (username == null || contact == null) return false;
            Contact c = await GetContact(username, contact);
            if (c == null) return false;

            Message m = c.Messages.FirstOrDefault(m => m.Id == id);
            if (m == null) return false;

            c.Messages.Remove(m);
            //(await GetMessages(username, contact)).Remove(m);
            _context.Update(c);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> UpdateTimeToAllUsers(string connected)
        {
            if(connected == null || connected.Length == 0) return false;

            // update the time for all contact rows where the connected user is the contact
            List<Contact> contact = await _context.Contact.Where(c => c.Id == connected).ToListAsync();
            contact.ForEach(c => { 
                c.LastSeen = DateTime.Now;
                _context.Update(c);
            });

            // change to the user himself
            User user = await Get(connected);
            user.LastSeen = DateTime.Now;
            _context.Update(user);

            await _context.SaveChangesAsync();
            return true;
        }
    }
}


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

        public async Task<Contact> GetContact(string username, string contact_name)
        {
            if (username == null || contact_name == null) return null;

            List<Contact> all_contacts = await GetContacts(username);
            if (all_contacts == null) return null;
            Contact contact = all_contacts.FirstOrDefault(x => x.Id == contact_name);
            return contact;
        }
        /*public async Task<List<Message>> GetContactMsgs(string username, string contact_name)
        {
            if(username == null || contact_name == null) return null;
            List<Contact> all_contacts = await GetContacts(username);
            if (all_contacts == null) return null;
            return all_contacts.FirstOrDefault(x => x.ContactUsername == contact_name).Messages;
             
        }*/
        public async Task<bool> AddContact(string username, string contact_name, string contact_nickname, string contact_server)
        {
            if (username == null || contact_name == null) return false;

            User user = await Get(username);
            if(user == null) return false;


            Contact c = await GetContact(username, contact_name);
            if (c != null) return false;

            if (await GetContacts(username) == null) user.Contacts = new List<Contact>();

            /*User contact_user = await Get(contact_name);
            if (contact_user == null) return false;*/

            //Contact contact = new (contact_name, contact_user);
            Contact contact = new();
            contact.TalkingTo = username;
            contact.Id = contact_name;
            contact.Name = contact_nickname;
            contact.Server = contact_server;
            contact.LastSeen = DateTime.Now;

            // _context.Contact.Add(contact); ??


            //_context.Contact.Add(contact);
            // TODO are we sure that the contacts are updated also on the DB ?
            user.Contacts.Add(contact);
            //_context.Update(user);
            
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
            // _context.Contact.Remove(contact);??

            _context.Update(user);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> ChangeContact(string username, string contact_username, string nickname, string server)
        {
            if (username==null || contact_username == null) return false;

            Contact c = await GetContact(username, contact_username);
            if(c == null) return false;

            c.Server = server;
            c.Name = nickname;

            _context.Update(c);
            await _context.SaveChangesAsync();
            /*c.User.Server = server;
            c.User.Nickname = nickname;*/
            return true;
        }


        /*public async Task<List<User>> GetContactsInfo(string username)
        {
            if (username == null) return null;
            List<Contact> contacts = await GetContacts(username);
            
            List<string> contacts_username = new List<string>();
            foreach(var contact in contacts)
            {
                contacts_username.Add(contact.ContactUsername);
            }

            var contact_info =  await (from user in _context.User
                                       where contacts_username.Contains(user.Username)
                                       select user).ToListAsync();

            return contact_info;
        }*/

        public async Task<Message> GetLast(string username, string contact)
        {
            Contact c = await GetContact(username, contact);
            if (username == null || contact == null || await Get(username) == null ||  c == null || c.Messages.Count == 0)
            {
                return null;
            }

            //Contact c = (await Get(username)).Contacts.Find(x => x.ContactUsername == contact);
            return c.Messages.Last();
        }

        public async Task<List<Message>> GetMessages(string username, string contact)
        {
            Contact c = await GetContact(username, contact);
            if (username == null || contact == null || await Get(username) == null || c == null) return null;
            if(await GetLast(username, contact) == null) return new List<Message>();
            return c.Messages;
        }

        public async Task<Message> GetMessageID(string username, string contact, int id)
        {
            if (username == null || contact == null || await Get(username) == null || await GetContact(username, contact) == null)
            {
                return null;
            }
            return (await GetMessages(username, contact)).FirstOrDefault(m => m.Id == id);
        }

        private async Task<bool> addMessageHelper(string username, string contacat, string data, bool isMine)
        {
            if (await Get(username) == null) return false;
            Contact c = await GetContact(username, contacat);
            if (c == null) return false;


            //Message message = new(id, "text", data, isMine);
            Message message = new();
            message.Type = "text";
            message.Data = data;
            message.IsMine = isMine;
            message.TimeSent = DateTime.Now;

            //_context.Message.Add(message);
            c.Messages.Add(message);
            c.LastMessage = data;
            _context.Add(message);
            _context.Update(c);
            await _context.SaveChangesAsync();

            return true;
        }

        public async Task<bool> AddMessage(string username, string contact, string data)
        {
            if (username == null || contact == null || data == null) return false;
            //maybe need to check if contact is in this server, if he is - add the message to his list as well, if he isnt - send a request to the other server.
            bool temp = await addMessageHelper(username, contact, data, true);
            if (temp) await _context.SaveChangesAsync();
            return temp;
        }

        

        /*public async Task<bool> UpdateMessage(string id, int id2, string username, string newData)
        {
            if (username == null || newData == null) return false;
            Contact c = await GetContact(username, id);
            if (c == null) return false;
            Message m = c.Messages.FirstOrDefault(m => m.Id == id2);
            m.Data = newData;
            await _context.SaveChangesAsync();
            return true;
        }*/
        public async Task<bool> UpdateMessage(string contact_username, int id, string username, string newData)
        {
            if (username == null || newData == null || contact_username == null) return false;
            Contact c = await GetContact(username, contact_username);
            if (c == null) return false;
            Message m = c.Messages.FirstOrDefault(m => m.Id == id);
            if (m == null) return false;
            m.Data = newData;
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
            (await GetMessages(username, contact)).Remove(m);
            //_context.Message.Remove(m);
            _context.Update(c);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> ReceiveMessage(string username, string contact, string data)
        {
            if (username == null || contact == null || data == null) return false;
            //maybe need to check if contact is in this server, if he is - add the message to his list as well, if he isnt - send a request to the other server.
            bool temp = await addMessageHelper(username, contact, data, false);
            if (temp) await _context.SaveChangesAsync();
            return temp;
        }

    }
}


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;

namespace Services
{
    public interface IUserService
    {
        public Task Add(User usern);
        public Task<bool> Exist(string username);
        public Task<User> Get(string username);
        public Task<List<Contact>> GetContacts(string username);
        public Task<Contact> GetContact(string username, string contact_name);
        public Task<bool> AddContact(string username, string contact_name, string contact_nickname, string contact_server);
        public Task<bool> DeleteContact(string username, string contact_name);
        //public Task<List<Message>> GetContactMsgs(string username, string contact_name);

        public Task<bool> ChangeContact(string username, string contact_username, string nickname, string server);

        public Task<List<Message>> GetMessages(string username, string contact);

        // This is the old ContactService methods
        public Task<Message> GetLast(string username, string contact);
        public Task<Message> GetMessageID(string username, string contact, int id);
        public Task<bool> AddMessage(string username, string contact, string data);
        public Task<bool> UpdateMessage(string contact_username, int id, string username, string newData);

        public Task<bool> DeleteMessage(string username, string contact, int id);

        public Task<bool> ReceiveMessage(string username, string contact, string data);
    }
}

/*using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;

namespace Services
{
    public interface IUserService
    {
        public Task Add(User usern);
        public Task<bool> Exist(string username);
        public Task<User> Get(string username);
        public Task<List<Contact>> GetContacts(string username);
        public Task<Contact> GetContact(string username, string contact_name);
        public Task<bool> AddContact(string username, string contact_name, string contact_nickname, string contact_server);
        public Task<bool> DeleteContact(string username, string contact_name);
        public Task<List<Message>> GetContactMsgs(string username, string contact_name);

        public Task<bool> ChangeContact(string username, string contact_username, string nickname, string server);

        public Task<List<Message>> GetMessages(string username, string contact);

        // This is the old ContactService methods
        public Task<Message> GetLast(string username, string contact);
        public Task<Message> GetMessageID(string username, string contact, int id);
        public Task<bool> AddMessage(string username, string contact, string data);
        public Task<bool> UpdateMessage(string id, int id2, string username, string newData);
    }
}
*/
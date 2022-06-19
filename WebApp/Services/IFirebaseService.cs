using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Domain;

namespace Services
{
    public interface IFirebaseService
    {
        public void AddUser(string username, string token);
        public void SendMessage(string username, Message message);
    }
}

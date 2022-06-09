using Microsoft.AspNetCore.SignalR;
using Domain;
using Services;

namespace WebApi.Hubs
{
    public class WebAppHub : Hub
    {
        private IUserService _service;

        public WebAppHub(IUserService service)
        {
            _service = service;
        }

        public async Task Login(string username)
        {
            await Groups.AddToGroupAsync(Context.ConnectionId, username);
        }

        // The Client is going to activate this function
        public async Task SendMessage(string message, string username, string contact_username)
        {
            if (message == null || username == null || contact_username == null) return;
            await _service.AddMessage(contact_username, username, message);
            await _service.ReceiveMessage(username, contact_username, message);
            //await Clients.All.SendAsync("ChangeRecieved", value);
        }
    }
}

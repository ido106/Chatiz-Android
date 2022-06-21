using Domain;
using Microsoft.Extensions.Configuration;
using Services;
using System.Net.Http.Json;
using System.Text.Json;

namespace WebApi.Services
{
    public class FirebaseService : IFirebaseService
    {
        private readonly HttpClient httpClient;
        private static Dictionary<string, string> Tokens = new Dictionary<string, string>();

        public FirebaseService(IConfiguration configuration)
        {
            string serverKey = $"key={configuration["firebaseServerKey"]}";
            string senderId = $"id={configuration["firebaseSenderId"]}";
            httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.TryAddWithoutValidation("Authorization", serverKey);
            httpClient.DefaultRequestHeaders.TryAddWithoutValidation("Sender", senderId);
        }


        public void AddUser(string username, string token)
        {
            Tokens[username] = token;
        }

        public void SendMessage(string username, Message message)
        {
            if (Tokens.ContainsKey(username))
            {
                string token = Tokens[username];
                var payload = new
                {
                    to = token,
                    priority = "high",
                    content_available = true,
                    notification = new
                    {
                        body = message.Content,
                        title = message.from,
                        badge = 1
                    },
                    data = new
                    {
                        sender = message.from,
                        content = message.Content,
                        created = message.TimeSent.ToString("dd/MM hh:mm"),
                        id = message.Id,
                        sent = message.Sent,
                    }
                };
                httpClient.PostAsJsonAsync("https://fcm.googleapis.com/fcm/send", payload, options: new JsonSerializerOptions() { PropertyNamingPolicy = JsonNamingPolicy.CamelCase });
            }
        }
    }
}
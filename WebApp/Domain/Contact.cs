using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Domain
{
    public class Contact
    {
        [Key]
        [Required]
        [JsonPropertyName("Id")]
        public string Id { get; set; }

        [Key]
        [Required]
        [JsonIgnore]
        [JsonPropertyName("TalkingTo")]
        public string TalkingTo { get; set; }

        [Required]
        [JsonPropertyName("Nickname")]
        public string Nickname { get; set; }

        [Required]
        [JsonPropertyName("Server")]

        public string  Server { get; set; }

        [JsonPropertyName("LastSeen")]

        public DateTime LastSeen { get; set; }
        [JsonPropertyName("LastMessage")]

        public string LastMessage { get; set; }

        [JsonIgnore]
        public List<Message> Messages { get; set; }
    }
}
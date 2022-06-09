using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Domain
{
    public class Contact
    {
        [Key]
        [Required]
        public string Id { get; set; }

        [Key]
        [Required]
        [JsonIgnore]
        public string TalkingTo { get; set; }

        [Required]
        public string Nickname { get; set; }

        [Required]
        public string  Server { get; set; }

        public DateTime LastSeen { get; set; }

        public string LastMessage { get; set; }

        [JsonIgnore]
        public List<Message> Messages { get; set; }
    }
}
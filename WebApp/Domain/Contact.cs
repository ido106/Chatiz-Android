using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Domain
{
    public class Contact
    {
        [Required]
        [Key]
        [JsonIgnore]
        public int IdContact { get; set; }
        [Required]
        public string Id { get; set; }
        [Required]
        [JsonIgnore]
        public string TalkingTo { get; set; }

        public string Name { get; set; }

        public string  Server { get; set; }

        public DateTime LastSeen { get; set; }

        public string LastMessage { get; set; }

        [JsonIgnore]
        public string ImgSrc { get; set; }

        [JsonIgnore]
        public List<Message> Messages { get; set; }

        /*[Required]
        public User User { get; set; }*/
    }
}
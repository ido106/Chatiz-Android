using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace Domain
{
    public class User
    {
        [Key]
        [Required]
        public string Username { get; set; }

        [Required]
        public string Nickname { get; set; }

        [Required]
        [DataType(DataType.Password)]
        [JsonIgnore]
        public string Password { get; set; }

        public string Server { get; set; }

        public DateTime LastSeen { get; set; }

        [JsonIgnore]
        public string ImgSrc { get; set; }

        [JsonIgnore]
        public List<Contact> Contacts { get; set; }

    }
}

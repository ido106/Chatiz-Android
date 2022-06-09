using System.ComponentModel.DataAnnotations;
using System;
namespace Domain
{
    public class Message
    {
        [Key]
        [Required]
        public string from { get; set; }

        [Key]
        [Required]
        public string to { get; set; }

        [Key]
        [Required]
        public int Id { get; set; }

        [Required]
        public string Content { get; set; }

        [Required]
        public DateTime TimeSent { get; set; }

        [Required]
        public bool Sent { get; set; }
    }
}
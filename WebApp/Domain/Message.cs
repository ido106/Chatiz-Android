using System.ComponentModel.DataAnnotations;
using System;
namespace Domain
{
    public class Message
    {
        [Key]
        [Required]
        public int Id { get; set; }

        [Required]
        public string Type { get; set; }

        [Required]
        public string Data { get; set; }

        [Required]
        public DateTime TimeSent { get; set; }

        [Required]
        public bool IsMine { get; set; }
    }
}
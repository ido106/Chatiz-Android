using System.ComponentModel.DataAnnotations;
using System;
using System.Text.Json.Serialization;

namespace Domain
{
    public class Message
    {
        [Key]
        [Required]
        [JsonPropertyName("from")]
        public string from { get; set; }

        [Key]
        [Required]
        [JsonPropertyName("to")]

        public string to { get; set; }

        [Key]
        [Required]
        [JsonPropertyName("Id")]

        public int Id { get; set; }

        [Required]
        [JsonPropertyName("Content")]

        public string Content { get; set; }

        [Required]
        [JsonPropertyName("TimeSent")]

        public DateTime TimeSent { get; set; }

        [Required]
        [JsonPropertyName("Sent")]

        public bool Sent { get; set; }
    }
}
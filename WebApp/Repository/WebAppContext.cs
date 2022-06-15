using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Domain;
using System.Diagnostics;

namespace Repository
{
    public class WebAppContext : DbContext
    {
        private const string connectionString = "Server=localhost;Port=3306;Database=WebAppDB;User=root;Password=P@$$W0rd";

        protected override void OnConfiguring (DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySql(connectionString, MariaDbServerVersion.AutoDetect(connectionString));
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            //Debugger.Launch();
            // configure primary keys
            modelBuilder.Entity<User>().HasKey(user => user.Username);

            modelBuilder.Entity<User>().HasMany(u => u.Contacts);
            //modelBuilder.Entity<User>().HasMany(u => u.Contacts).WithOne(c => c.User).HasForeignKey(c => c.ContactUsername);

            modelBuilder.Entity<Contact>()
            .Property(s => s.Id).IsRequired();
            modelBuilder.Entity<Contact>().HasKey(contact => new
            {
                contact.TalkingTo, contact.Id
            });
            modelBuilder.Entity<Contact>().HasMany(c => c.Messages);
            //modelBuilder.Entity<Contact>().HasOne(c => c.User).WithMany(u => u.Contacts).HasForeignKey(c => c.ContactUsername);

            modelBuilder.Entity<Message>().HasKey(message => new
            {
                message.Id,
                message.to,
                message.from
            });
        }


        public DbSet<User> User { get; set; }

        public DbSet<Contact> Contact { get; set; }

        public DbSet<Message> Message { get; set; }
    }
}

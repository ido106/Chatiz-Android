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
        
        //public WebAppContext (DbContextOptions<WebAppContext> options)
        //    : base(options)
        //{
        //}

        private const string connectionString = "Server=localhost;Port=3306;Database=WebAppDB;User=root;Password=2056";

        /**
        public WebAppContext()
        {
            Database.EnsureCreated();
        }
        **/

        protected override void OnConfiguring (DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseMySql(connectionString, MariaDbServerVersion.AutoDetect(connectionString));
        }

        /**
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Contact>().HasKey(e => e.Username);
        }
        **/

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            //Debugger.Launch();
            // configure primary keys
            modelBuilder.Entity<User>().HasKey(user => user.Username);

            modelBuilder.Entity<User>().HasMany(u => u.Contacts);
            //modelBuilder.Entity<User>().HasMany(u => u.Contacts).WithOne(c => c.User).HasForeignKey(c => c.ContactUsername);

            modelBuilder.Entity<Contact>()
            .Property(s => s.IdContact).IsRequired();
            modelBuilder.Entity<Contact>().HasKey(c => c.IdContact);
            //modelBuilder.Entity<Contact>().HasOne(c => c.User).WithMany(u => u.Contacts).HasForeignKey(c => c.ContactUsername);
            modelBuilder.Entity<Contact>().HasMany(c => c.Messages);

            modelBuilder.Entity<Message>().HasKey(message => message.Id);
        }


        public DbSet<User> User { get; set; }

        public DbSet<Contact> Contact { get; set; }

        public DbSet<Message> Message { get; set; }
    }
}

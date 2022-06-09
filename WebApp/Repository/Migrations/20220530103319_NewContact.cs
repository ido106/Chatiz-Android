using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Repository.Migrations
{
    public partial class NewContact : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Contact_User_ContactUsername",
                table: "Contact");

            migrationBuilder.AddColumn<string>(
                name: "ImgSrc",
                table: "Contact",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<DateTime>(
                name: "LastSeen",
                table: "Contact",
                type: "datetime(6)",
                nullable: false,
                defaultValue: new DateTime(1, 1, 1, 0, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.AddColumn<string>(
                name: "Nickname",
                table: "Contact",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "Server",
                table: "Contact",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "Username",
                table: "Contact",
                type: "varchar(255)",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.CreateIndex(
                name: "IX_Contact_Username",
                table: "Contact",
                column: "Username");

            migrationBuilder.AddForeignKey(
                name: "FK_Contact_User_Username",
                table: "Contact",
                column: "Username",
                principalTable: "User",
                principalColumn: "Username");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Contact_User_Username",
                table: "Contact");

            migrationBuilder.DropIndex(
                name: "IX_Contact_Username",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "ImgSrc",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "LastSeen",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "Nickname",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "Server",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "Username",
                table: "Contact");

            migrationBuilder.AddForeignKey(
                name: "FK_Contact_User_ContactUsername",
                table: "Contact",
                column: "ContactUsername",
                principalTable: "User",
                principalColumn: "Username",
                onDelete: ReferentialAction.Cascade);
        }
    }
}

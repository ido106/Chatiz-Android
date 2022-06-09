using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Repository.Migrations
{
    public partial class UDATECONTACT : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Message_Contact_ContactUsername",
                table: "Message");

            migrationBuilder.DropIndex(
                name: "IX_Message_ContactUsername",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Contact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "ContactUsername",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "ContactUsername",
                table: "Contact");

            migrationBuilder.RenameColumn(
                name: "Nickname",
                table: "Contact",
                newName: "Name");

            migrationBuilder.AddColumn<int>(
                name: "ContactIdContact",
                table: "Message",
                type: "int",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "IdContact",
                table: "Contact",
                type: "int",
                nullable: false,
                defaultValue: 0)
                .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<string>(
                name: "Id",
                table: "Contact",
                type: "longtext",
                nullable: false)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "TalkingTo",
                table: "Contact",
                type: "longtext",
                nullable: false)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Contact",
                table: "Contact",
                column: "IdContact");

            migrationBuilder.CreateIndex(
                name: "IX_Message_ContactIdContact",
                table: "Message",
                column: "ContactIdContact");

            migrationBuilder.AddForeignKey(
                name: "FK_Message_Contact_ContactIdContact",
                table: "Message",
                column: "ContactIdContact",
                principalTable: "Contact",
                principalColumn: "IdContact");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Message_Contact_ContactIdContact",
                table: "Message");

            migrationBuilder.DropIndex(
                name: "IX_Message_ContactIdContact",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Contact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "ContactIdContact",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "IdContact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "Id",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "TalkingTo",
                table: "Contact");

            migrationBuilder.RenameColumn(
                name: "Name",
                table: "Contact",
                newName: "Nickname");

            migrationBuilder.AddColumn<string>(
                name: "ContactUsername",
                table: "Message",
                type: "varchar(255)",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "ContactUsername",
                table: "Contact",
                type: "varchar(255)",
                nullable: false,
                defaultValue: "")
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Contact",
                table: "Contact",
                column: "ContactUsername");

            migrationBuilder.CreateIndex(
                name: "IX_Message_ContactUsername",
                table: "Message",
                column: "ContactUsername");

            migrationBuilder.AddForeignKey(
                name: "FK_Message_Contact_ContactUsername",
                table: "Message",
                column: "ContactUsername",
                principalTable: "Contact",
                principalColumn: "ContactUsername");
        }
    }
}

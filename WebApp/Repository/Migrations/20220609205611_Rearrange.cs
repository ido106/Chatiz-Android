using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Repository.Migrations
{
    public partial class Rearrange : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Message_Contact_ContactIdContact",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Message",
                table: "Message");

            migrationBuilder.DropIndex(
                name: "IX_Message_ContactIdContact",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Contact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "ImgSrc",
                table: "User");

            migrationBuilder.DropColumn(
                name: "ContactIdContact",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "Data",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "IdContact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "ImgSrc",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "Name",
                table: "Contact");

            migrationBuilder.RenameColumn(
                name: "Type",
                table: "Message",
                newName: "Content");

            migrationBuilder.RenameColumn(
                name: "IsMine",
                table: "Message",
                newName: "Sent");

            migrationBuilder.UpdateData(
                table: "User",
                keyColumn: "Server",
                keyValue: null,
                column: "Server",
                value: "");

            migrationBuilder.AlterColumn<string>(
                name: "Server",
                table: "User",
                type: "longtext",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "longtext",
                oldNullable: true)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<int>(
                name: "Id",
                table: "Message",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .OldAnnotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<string>(
                name: "to",
                table: "Message",
                type: "varchar(255)",
                nullable: false,
                defaultValue: "")
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "ContactTalkingTo",
                table: "Message",
                type: "varchar(255)",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "from",
                table: "Message",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<string>(
                name: "TalkingTo",
                table: "Contact",
                type: "varchar(255)",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "longtext")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.UpdateData(
                table: "Contact",
                keyColumn: "Server",
                keyValue: null,
                column: "Server",
                value: "");

            migrationBuilder.AlterColumn<string>(
                name: "Server",
                table: "Contact",
                type: "longtext",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "longtext",
                oldNullable: true)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<string>(
                name: "Id",
                table: "Contact",
                type: "longtext",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "longtext")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "Nickname",
                table: "Contact",
                type: "longtext",
                nullable: false)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Message",
                table: "Message",
                column: "to");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Contact",
                table: "Contact",
                column: "TalkingTo");

            migrationBuilder.CreateIndex(
                name: "IX_Message_ContactTalkingTo",
                table: "Message",
                column: "ContactTalkingTo");

            migrationBuilder.AddForeignKey(
                name: "FK_Message_Contact_ContactTalkingTo",
                table: "Message",
                column: "ContactTalkingTo",
                principalTable: "Contact",
                principalColumn: "TalkingTo");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Message_Contact_ContactTalkingTo",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Message",
                table: "Message");

            migrationBuilder.DropIndex(
                name: "IX_Message_ContactTalkingTo",
                table: "Message");

            migrationBuilder.DropPrimaryKey(
                name: "PK_Contact",
                table: "Contact");

            migrationBuilder.DropColumn(
                name: "to",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "ContactTalkingTo",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "from",
                table: "Message");

            migrationBuilder.DropColumn(
                name: "Nickname",
                table: "Contact");

            migrationBuilder.RenameColumn(
                name: "Sent",
                table: "Message",
                newName: "IsMine");

            migrationBuilder.RenameColumn(
                name: "Content",
                table: "Message",
                newName: "Type");

            migrationBuilder.AlterColumn<string>(
                name: "Server",
                table: "User",
                type: "longtext",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "longtext")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "ImgSrc",
                table: "User",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<int>(
                name: "Id",
                table: "Message",
                type: "int",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int")
                .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<int>(
                name: "ContactIdContact",
                table: "Message",
                type: "int",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "Data",
                table: "Message",
                type: "longtext",
                nullable: false)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<string>(
                name: "Server",
                table: "Contact",
                type: "longtext",
                nullable: true,
                oldClrType: typeof(string),
                oldType: "longtext")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.UpdateData(
                table: "Contact",
                keyColumn: "Id",
                keyValue: null,
                column: "Id",
                value: "");

            migrationBuilder.AlterColumn<string>(
                name: "Id",
                table: "Contact",
                type: "longtext",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "longtext",
                oldNullable: true)
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AlterColumn<string>(
                name: "TalkingTo",
                table: "Contact",
                type: "longtext",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "varchar(255)")
                .Annotation("MySql:CharSet", "utf8mb4")
                .OldAnnotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<int>(
                name: "IdContact",
                table: "Contact",
                type: "int",
                nullable: false,
                defaultValue: 0)
                .Annotation("MySql:ValueGenerationStrategy", MySqlValueGenerationStrategy.IdentityColumn);

            migrationBuilder.AddColumn<string>(
                name: "ImgSrc",
                table: "Contact",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddColumn<string>(
                name: "Name",
                table: "Contact",
                type: "longtext",
                nullable: true)
                .Annotation("MySql:CharSet", "utf8mb4");

            migrationBuilder.AddPrimaryKey(
                name: "PK_Message",
                table: "Message",
                column: "Id");

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
    }
}

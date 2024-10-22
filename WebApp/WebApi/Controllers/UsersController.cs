﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Domain;
using Repository;
using Microsoft.AspNetCore.Authorization;
using Services;
using System.Security.Claims;
using Microsoft.IdentityModel.JsonWebTokens;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using System.IdentityModel.Tokens.Jwt;
using JwtRegisteredClaimNames = System.IdentityModel.Tokens.Jwt.JwtRegisteredClaimNames;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using System.Text.Json;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api/")]
    public class UsersController : ControllerBase
    {
        private IFirebaseService _firebaseService;
        private IUserService _service;
        private IConfiguration _config;
        private JwtSecurityTokenHandler _jwtHandler;

        public UsersController(IUserService service, IFirebaseService firebase, IConfiguration config)
        {
            _service = service;
            _config = config;
            _firebaseService = firebase;
            _jwtHandler = new JwtSecurityTokenHandler();
        }


        private string getConnectedUser()
        {
            var token = _jwtHandler.ReadJwtToken(Request.Headers["Authorization"].ToString().Substring("Bearer ".Length));
            string username = token.Claims.First(claim => claim.Type == "username").Value;
            return username;
        }

        // GET: Users
        [HttpGet("contacts")]
        [Authorize]

        public async Task<IActionResult> GetContacts()
        {
            string username = getConnectedUser();
            if (username == null) return NotFound();
            if (await _service.Get(username) == null) return NotFound();
            List<Contact> contacts = await _service.GetContacts(username);
            if(contacts == null) { 
                return Ok(new List<Contact>());
            }
            return Ok(contacts);
        }

        [HttpPost("contacts")]
        [Authorize]

        public async Task<IActionResult> AddContact([FromBody] JsonElement json)
        {
            string username = getConnectedUser();
            string contact_username;
            string contact_nickname;
            string contact_server;
            if (username == null || await _service.Get(username) == null) return NotFound();

            try {
                contact_username = json.GetProperty("id").ToString();
                contact_nickname = json.GetProperty("name").ToString();
                contact_server = json.GetProperty("server").ToString();
            } catch(Exception e)
            {
                return BadRequest(e.Message);
            }
            
            if (contact_username == null || contact_nickname == null || contact_server == null) return NotFound();

            await _service.AddContact(username, contact_username, contact_nickname, contact_server);

            return StatusCode(201);
        }




        [HttpGet("contacts/{id}")]
        [Authorize]
        public async Task<IActionResult> ContactID([Bind("id")] string contact)
        {
            string username = getConnectedUser();
            if (username == null || await _service.Get(username) == null) return NotFound();

            if (contact == null) return NotFound();

            Contact c = await _service.GetContact(username, contact);

            if (c == null) return NotFound();

            return Ok(c);
        }

        [HttpPut("contacts/{id}")]
        [Authorize]
        public async Task<IActionResult> ChangeContactID([Bind("id")] string id, [FromBody] JsonElement json)
        {
            string username = getConnectedUser();
            if (username == null || await _service.Get(username) == null) return NotFound();
            if (id == null) return NotFound();
            Contact c = await _service.GetContact(username, id);
            if (c == null) return NotFound();

            string nickname;
            string server;
            try
            {
                nickname = json.GetProperty("name").ToString();
                server = json.GetProperty("server").ToString();
            } catch (Exception e)
            {
                return BadRequest(e.Message);
            }
            // TODO do i have to change directly the contact's user? (it will change to the original user also)
            await _service.ChangeContact(username, id, nickname , server);

            return NoContent();
        }

        [HttpDelete("contacts/{id}")]
        [Authorize]
        public async Task<IActionResult> DeleteContactID([Bind("id")] string id)
        {
            string username = getConnectedUser();
            if (username == null || await _service.Get(username) == null) return NotFound();
            if (id == null || await _service.GetContact(username, id) == null) return NotFound();


            await _service.DeleteContact(username, id);

            return NoContent();
        }

        [HttpGet("contacts/{id}/messages")]
        [Authorize]
        public async Task<IActionResult> GetMessages([Bind("id")] string id)
        {
            string username = getConnectedUser();
            if (username == null || await _service.Get(username) == null) return NotFound();

            if (id == null || await _service.GetContact(username, id) == null) return NotFound();
            List<Message> msgs = await _service.GetMessages(username, id);
            if (msgs == null)
            {
                return Ok(msgs);
            }
            return Ok(msgs);
        }

        [HttpPost("contacts/{id}/messages")]
        [Authorize]
        public async Task<IActionResult> AddMessage([Bind("id")] string id, [FromBody] JsonElement json)
        {

            string username = getConnectedUser();
            if (username == null || await _service.Get(username) == null) return NotFound();

            if (id == null || await _service.GetContact(username, id) == null) return NotFound();

            string content;
            try
            {
                content = json.GetProperty("content").ToString();
            } catch (Exception e)
            {
                return BadRequest(e.Message);
            }

            await _service.AddMessage(username, id, content, true);
            //await _service.UpdateTimeToAllUsers(username);

            return StatusCode(201);
        }

        [HttpGet("contacts/{id}/messages/{id2}")]
        [Authorize]
        public async Task<IActionResult> GetMessageID([Bind("id")] string id, [Bind("id2")] string id2)
        {
            string username = getConnectedUser();
            if (id2 == null || username == null || await _service.Get(username) == null) return NotFound();

            if (id == null || await _service.GetContact(username, id) == null) return NotFound();

            if (!int.TryParse(id2, out var id3)) return BadRequest();

            Message message = await _service.GetMessageID(username, id, id3);
            if (message == null) return NotFound();

            return Ok(message);
        }

        [HttpPut("contacts/{id}/messages/{id2}")]
        [Authorize]
        public async Task<IActionResult> EditMessageID([Bind("id")] string id, [Bind("id2")] string id2, [FromBody] JsonElement json)
        {
            string username = getConnectedUser();
            if (id2 == null || username == null || await _service.Get(username) == null) return NotFound();

            if (id == null || await _service.GetContact(username, id) == null) return NotFound();

            if (!int.TryParse(id2, out var id3)) return BadRequest();

            Message message = await _service.GetMessageID(username, id, id3);
            if (message == null) return NotFound();

            string content;
            try
            {
                content = json.GetProperty("content").ToString();
            } catch (Exception e)
            {
                return BadRequest(e.Message);
            }

            await _service.UpdateMessage(id, id3, username, content);

            return NoContent();
        }

        [HttpDelete("contacts/{id}/messages/{id2}")]
        [Authorize]
        public async Task<IActionResult> DeleteMessageID([Bind("id")] string id, [Bind("id2")] string id2)
        {
            string username = getConnectedUser();
            if (id2 == null || username == null || await _service.Get(username) == null) return NotFound();

            if (id == null || await _service.GetContact(username, id) == null) return NotFound();

            if (!int.TryParse(id2, out var id3)) return BadRequest();

            Message message = await _service.GetMessageID(username, id, id3);
            if (message == null) return NotFound();

            await _service.DeleteMessage(username, id, id3);

            return NoContent();
        }

        [HttpPost("invitations")]
        public async Task<IActionResult> NewChatInvitation([FromBody] JsonElement json)
        {
            string from;
            string to;
            string server;

            try
            {
                from = json.GetProperty("from").ToString();
                to = json.GetProperty("to").ToString();
                server = json.GetProperty("server").ToString();
            } catch (Exception e)
            {
                return BadRequest(e.Message);
            }

            if (await _service.Get(to) == null) return BadRequest();
            if (await _service.GetContact(to, from) != null) return Ok();

            await _service.AddContact(to, from, from, server);
            return StatusCode(201);
        }

        [HttpPost("transfer")]
        public async Task<IActionResult> NewMessageTransfer([FromBody] JsonElement json)
        {
            string from;
            string to;
            string content;

            try
            {
                from = json.GetProperty("from").ToString();
                to = json.GetProperty("to").ToString();
                content = json.GetProperty("content").ToString();
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }

            if (await _service.Get(to) == null) return BadRequest();
            if (await _service.GetContact(to, from) == null) return BadRequest();

            await _service.AddMessage(to, from, content, false);

            Message message = await _service.GetLast(to, from);
            _firebaseService.SendMessage(to, message); // send firebase

            return StatusCode(201);
        }

        // *****************

        [HttpPost("SignIn")]
        public async Task<IActionResult> SignIn([FromBody] JsonElement json)
        {
            string username;
            string password;
            string firebaseToken;
            try
            {
                username = json.GetProperty("username").ToString();
                password = json.GetProperty("password").ToString();
                firebaseToken = json.GetProperty("token").ToString();

            } catch (Exception e)
            {
                return BadRequest(e.Message);
            }
            
            if (await _service.Get(username) != null && (await _service.Get(username)).Password.Equals(password))
            {
                await _service.UpdateTimeToAllUsers(username);
                // insert token
                _firebaseService.AddUser(username, firebaseToken);

                var claims = new[]
                {
                    new Claim(JwtRegisteredClaimNames.Sub, _config["JWTParams:Subject"]),
                    new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                    new Claim(JwtRegisteredClaimNames.Iat, DateTime.UtcNow.ToString()),
                    new Claim("username", username)
                };


                var claimsIdentity = new ClaimsIdentity(claims, JwtBearerDefaults.AuthenticationScheme);
                var authProperties = new AuthenticationProperties() { ExpiresUtc = DateTime.UtcNow.AddDays(7) };

                var secretKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["JWTParams:SecretKey"]));
                var mac = new SigningCredentials(secretKey, SecurityAlgorithms.HmacSha256);
                var token = new JwtSecurityToken(_config["JWTParams:Issuer"], _config["JWTParams:Audience"], claims, expires: DateTime.UtcNow.AddDays(7), signingCredentials: mac);
                return Ok(new JwtSecurityTokenHandler().WriteToken(token));
            }
            return BadRequest();
        }

        [HttpPost("Register")]
        public async Task<IActionResult> Register([FromBody] JsonElement json)
        {
            if (ModelState.IsValid)
            {
                User user = new();
                string username;
                string nickname;
                string password;
                try
                {
                    username = json.GetProperty("username").ToString();
                    nickname = json.GetProperty("nickName").ToString();
                    password = json.GetProperty("password").ToString();
                } catch (Exception e)
                {
                    return BadRequest(e.Message);
                }
                user.Username = username;
                user.Nickname = nickname;
                user.Password = password;
                user.Contacts = new List<Contact>();

                var q = await _service.Get(user.Username);
                // user is already exist
                if (q != null) return BadRequest();

                user.LastSeen = DateTime.Now;
                user.Server = "https://localhost:7092";

                await _service.Add(user);
                return Ok();
            }
            return BadRequest();
        }

        [HttpPost("GetUser")]
        public async Task<IActionResult> GetUser([FromBody] JsonElement json)
        {
            string username;
            try
            {
                username = json.GetProperty("username").ToString();
            }
            catch (Exception e)
            {
                return BadRequest(e.Message);
            }

            var q = await _service.Get(username);
            // if user doesnt exist
            if (q == null) return BadRequest();

            return Ok(q);
        }
    }
}
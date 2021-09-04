<template>
<div>
  <custom-nav-bar :value="vCode" @on-input="handleInput">

  </custom-nav-bar>
  <div class="container">
    <div class="row g-5 align-items-end">
      <div class="col">
        <select  v-model="selectedType">
          <option value="">Select type</option>
          <option v-for="type in types" :key="type" :value="type">{{type}}</option>
        </select>
      </div>
      <div class="col ">
        <select v-model="selectedRole">
          <option value="">Select role</option>
          <option v-for="role in roles" :key="role" :value="role">{{role}}</option>
        </select>
      </div>
      <div class="col ">
      <p @click="sort('username')">Username<span v-if="sortBy == 'username'" v-html="sortSymbol"></span></p>
      </div>
      <div class="col ">
      <p @click="sort('name')">First name<span v-if="sortBy == 'firstName'" v-html="sortSymbol"></span></p>
      </div>
      <div class="col ">
      <p @click="sort('surname')">Last name<span v-if="sortBy == 'lastName'" v-html="sortSymbol"></span></p>
      </div>
      <div class="col ">
      <p @click="sort('points')">Points<span v-if="sortBy == 'points'" v-html="sortSymbol"></span></p>
      </div>
    </div>

  <div class="row row-cols-1 row-cols-3 g-2">
    <div class="col" v-for="user in filteredUsers">
      <div v-if="!user.isBlocked" class="card  text-white bg-dark">
        <img v-if="user.profileImage" :src="'http://localhost:8080/image/' + user.profileImg" class="card-img-top h-100 w-100" alt="...">
        <img v-else src="images/profile-pic-placeholder.png" class="card-img-top h-100 w-100" alt="...">        <div class="card-body">
          <h5 class="card-title">{{user.username}}</h5>
          <p class="card-text">Ime: {{user.name}} Surname: {{user.surname}} Gender: {{user.genderType }}
            Role: {{user.userRoleType}} Date of birth: {{user.dateOfBirth}}
          </p>
          <div class="btn-group" role="group">
              <button  v-if="user.userRoleType != 'Admin'" @click="deleteUser(user.username)" class="btn btn-secondary">Delete</button>
              <button  v-if="user.userRoleType != 'Admin' && !user.isBlocked" @click="blockUser(user.username)" class="btn btn-secondary">Block</button>
            <button  v-else-if="user.userRoleType != 'Admin' && user.isBlocked" @click="unblockUser(user.username)" class="btn btn-secondary">Unblock</button>
          </div>
        </div>
      </div>
      <div v-else-if="user.orderCancelCount >= 5" class="card  text-white bg-warning">
        <img v-if="user.profileImage" :src="'http://localhost:8080/image/' + user.profileImg" class="card-img-top h-100 w-100" alt="...">
        <img v-else src="images/profile-pic-placeholder.png" class="card-img-top h-100 w-100" alt="...">
        <div class="card-body">
          <h5 class="card-title">{{user.username}}</h5>
          <p class="card-text">Ime: {{user.name}} Surname: {{user.surname}} Gender: {{user.genderType }}
            Role: {{user.userRoleType}} Date of birth: {{user.dateOfBirth}}
          </p>
          <div class="btn-group" role="group">
            <button  v-if="user.userRoleType != 'Admin'" @click="deleteUser(user.username)" class="btn btn-secondary">Delete</button>
            <button  v-if="user.userRoleType != 'Admin' && !user.isBlocked" @click="blockUser(user.username)" class="btn btn-secondary">Block</button>
            <button  v-else-if="user.userRoleType != 'Admin' && user.isBlocked" @click="unblockUser(user.username)" class="btn btn-secondary">Unblock</button>
          </div>
        </div>
      </div>
      <div v-else class="card  text-white bg-danger">
        <img v-if="user.profileImage" :src="'http://localhost:8080/image/' + user.profileImg" class="card-img-top h-100 w-100" alt="...">
        <img v-else src="images/profile-pic-placeholder.png" class="card-img-top h-100 w-100" alt="...">
        <div class="card-body">
          <h5 class="card-title">{{user.username}}</h5>
          <p class="card-text">Ime: {{user.name}} Surname: {{user.surname}} Gender: {{user.genderType }}
            Role: {{user.userRoleType}} Date of birth: {{user.dateOfBirth}}
          </p>
          <div class="btn-group" role="group">
            <button  v-if="user.userRoleType != 'Admin'" @click="deleteUser(user.username)" class="btn btn-secondary">Delete</button>
            <button  v-if="user.userRoleType != 'Admin' && !user.isBlocked" @click="blockUser(user.username)" class="btn btn-secondary">Block</button>
            <button  v-else-if="user.userRoleType != 'Admin' && user.isBlocked" @click="unblockUser(user.username)" class="btn btn-secondary">Unblock</button>
          </div>
        </div>
      </div>
    </div>
    </div>
  </div>
</div>
</template>

<script>
module.exports =
    {
      data: function (){
        return{
          users:[],
          profilepicture: '',
          queryparamusers:'',
          vCode: '',
          selectedType: '',
          selectedRole: '',
          sortBy: 'username',
          sortDirection: 'asc',
          roles: ["ADMIN", "MANAGER", "BUYER", "DELIVERER"],
          types: ["BRONZE", "SILVER", "GOLD"],
          }
      },
      methods:{

        handleInput(value){
          this.vCode = value;

        },
        blockUser: function(username) {
          if(!localStorage.jws){
            this.$router.push('/')
            return;
          }
          axios.post('/admin/user/block/' + username, {}, {headers:{'Authorization': 'Bearer' + localStorage.jws}})
          .then(() => this.getUsers())
          .catch(r => console.log(r));
          },
        unblockUser: function(username) {
          if(!localStorage.jws){
            this.$router.push('/')
            return;
          }
          axios.post('/admin/user/unblock/' + username,{}, {headers:{'Authorization': 'Bearer' + localStorage.jws}})
              .then(() => this.getUsers())
              .catch(r => console.log(r));
        },
        deleteUser: function (username){
          if(!localStorage.jws){
            this.$router.push('/')
            return
          }

          axios.delete('/admin/user/' + username, {headers:{'Authorization': 'Bearer' +localStorage.jws}})
          .then(() => this.getUsers())
          .catch(r => console.log(r));

        },
        sort: function(param){
          if(this.sortBy == param) {
            this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
          }
          this.sortBy = param;
          console.log(this.sortBy)
          let sortedUsers = this.users;
          this.users.sort((a, b) => {
            let aval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, a);
            aval = aval.toLowerCase();
            let bval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, b);
            bval = bval.toLowerCase();

            let modifier = 1;
            if(this.sortDirection == 'desc') {
              modifier = -1;
            }
            if(aval < bval) {
              return -1 * modifier;
            }
            if(aval > bval) {
              return modifier;
            }
            return 0;
          });
        },
        getFilteredByRole: function(users, param){
            if(!param)
              return users;
            return users.filter(user => user.userRoleType.toLowerCase().indexOf(param.toLowerCase()) >-1);
        },
        getFilteredByType: function (users, param){
          if(!param)
            return users;
            return users.filter(user => user.buyerTypeRank.toLowerCase().indexOf(param.toLowerCase()) > -1);
          },

        getFilteredByName: function (users, params){
          let filteredByName = [];
          if(params[0] === ''  || params[0] == ' '){
            filteredByName =  users;
          }else if(params[1] == null || params[1] == ' '){
            filteredByName =    users.filter((user) => user.name.toLowerCase().indexOf(params[0].toLowerCase()) > -1 ||
                                user.username.toLowerCase().indexOf(params[0].toLowerCase()) > -1 ||
                                user.surname.toLowerCase().indexOf(params[0].toLowerCase()) > -1
                              );

          }else if(params[2] == null || params[2] == ' '){
            filteredByName =  users.filter((user) =>
              user.name.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[1].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[0].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[1].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[0].toLowerCase()) > -1 ||
                user.surname.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[1].toLowerCase()) > -1 ||
                user.surname.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[0].toLowerCase()) > -1
            );
          }else{
              //Problem reisit tako sto se koristi && za  name0 surname 1 i username 3
            filteredByName =  users.filter((user) =>

              user.name.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
              user.surname.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
              user.username.toLowerCase().indexOf(params[2].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[2].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[1].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[2].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[2].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[0].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[2].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[0].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[1].toLowerCase()) > -1 ||
                user.name.toLowerCase().indexOf(params[2].toLowerCase()) > -1 &&
                user.surname.toLowerCase().indexOf(params[1].toLowerCase()) > -1 &&
                user.username.toLowerCase().indexOf(params[0].toLowerCase()) > -1

            );
          }
          return filteredByName

        },

        getUsers: function (){
          if(!localStorage.jws){
            return;
          }
          this.users = []

          axios.get('/admin/users', {headers: {'Authorization': 'Bearer' + localStorage.jws}})
          .then(r =>
              {
                let users = r.data;
                users.forEach(u =>
                {

                  if(!u.points){
                    u.points = 0.0;
                  }
                  if(!u.buyerTypeRank){
                    u.buyerTypeRank = 'None';
                  }

                });
                this.users = [...users];
              }
          ).catch(r => console.log(r));
        },
        },computed: {

        filteredUsers: function (){



          return this.getFilteredByType(
                 this.getFilteredByRole(
                 this.getFilteredByName(this.users, this.vCode.split(' ')), this.selectedRole), this.selectedType);
        },
        sortSymbol: function() {
          return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
        },

      },
      mounted(){
        this.getUsers();
      },
    };
</script>
<style scoped>
#user-view-main{
  padding: 100px;
}
#link{
  text-decoration: none;
  color: black;
}
.row{
  margin-right: 0px;
}
</style>


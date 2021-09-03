<template>
<div>
  <custom-nav-bar :value="vCode" @on-input="handleInput">

  </custom-nav-bar>
  <div id="user-view-main">
    <div id="sort-container">
      <p>Username<span v-if="sortBy == 'username'" ></span></p>
      <p>First name<span v-if="sortBy == 'firstName'" ></span></p>
      <p >Last name<span v-if="sortBy == 'lastName'"></span></p>
      <p >Points<span v-if="sortBy == 'points'" ></span></p>
    </div>
    <div class="filter-container">
      <select  v-model="selectedType">
        <option value="">Select type</option>
        <option v-for="type in types" :key="type" :value="type">{{type}}</option>
      </select>
      <select v-model="selectedRole">
        <option value="">Select role</option>
        <option v-for="role in roles" :key="role" :value="role">{{role}}</option>
      </select>
    </div>
  <div class="row row-cols-1 row-cols-5 g-4">
    <div class="col" v-for="user in filteredUsers">
      <div class="card" >
        <img v-bind:src="user.profileImage" class="card-img-top h-100 w-100" alt="...">
        <div class="card-body">
          <h5 class="card-title">{{user.username}}</h5>
          <p class="card-text">Ime: {{user.name}} Surname: {{user.surname}} Gender: {{user.genderType }}
            Role: {{user.userRoleType}} Date of birth: {{user.dateOfBirth}}
          </p>

        </div>
        </router-link>

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
          roles: ["ADMIN", "MANAGER", "REGULAR"],
          types: ["BRONZE", "SILVER", "GOLD"],
        }
      },
      methods:{

        handleInput(value){
          this.vCode = value;

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
                  if(u.genderType == 'MALE'){
                  axios.get('https://randomuser.me/api/?gender=male').then(r => {
                    u.profileImage = r.data.results[0].picture.large;
                  });}
                  if(u.genderType == 'FEMALE'){
                    axios.get('https://randomuser.me/api/?gender=female').then(r => {
                      u.profileImage = r.data.results[0].picture.large;
                    });
                  }
                  if(u.genderType == 'OTHER'){
                    axios.get('https://randomuser.me/api/?inc=picture').then(r => {
                      u.profileImage = r.data.results[0].picture.large;
                    });
                  }

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


          return this.getFilteredByName(this.users, this.vCode.split(' '))
        }

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


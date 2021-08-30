<template>
<div>
  <custom-nav-bar :value="vCode" @on-input="handleInput">

  </custom-nav-bar>
  <div id="user-view-main">
    <div id="sort-container">
      <p @click="sort('username')">Username<span v-if="sortBy == 'username'" v-html="sortSymbol"></span></p>
      <p @click="sort('firstName')">First name<span v-if="sortBy == 'firstName'" v-html="sortSymbol"></span></p>
      <p @click="sort('lastName')">Last name<span v-if="sortBy == 'lastName'" v-html="sortSymbol"></span></p>
      <p @click="sort('points')">Points<span v-if="sortBy == 'points'" v-html="sortSymbol"></span></p>
    </div>
    <div class="filter-container">
      <select @change="filterByRole" v-model="selectedType">
        <option value="">Select type</option>
        <option v-for="type in types" :key="type" :value="type">{{type}}</option>
      </select>
      <select v-model="selectedRole">
        <option value="">Select role</option>
        <option v-for="role in roles" :key="role" :value="role">{{role}}</option>
      </select>
    </div>
    <button @click="selectedroleshow"> lick me</button>
  <div class="row row-cols-1 row-cols-5 g-4">
    <div class="col" v-for="user in filteredUsers">
      <div class="card" >
        <router-link id="link" to="/home + {{user.name}}">
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
        selectedroleshow: function (event){
          console.log(event.target.value)
        },
        handleInput(value){
          this.vCode = value;
          console.log(value);

        },
        sort: function(s) {
          if(this.sortBy == s) {
            this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
          }
          this.sortBy = s;
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
        filterByRole: function (){
        console.log(this.selectedRole)
        }
      },computed: {
        filteredUsers: function () {
          let res = [...this.users];

          let queryParam = this.vCode.split(' ')
          if (queryParam.length == 1) {
            return res.filter((user) => {
              return user.username.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[0].toLowerCase());
            });
          } else if (queryParam.length == 2) {
            return res.filter((user) => {
              return user.username.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.username.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[1].toLowerCase())
                  ;
            });
          } else if (queryParam.length == 3) {
            return res.filter((user) => {
              return user.username.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.username.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.username.toLowerCase().match(queryParam[2].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[2].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[2].toLowerCase())
                  ;
            });
          }
          if(selectedRole == 'ADMIN'){
            return res.filter((user) => {
              console.log(user.userRoleType)
              return user.userRoleType.match('ADMIN');
            })
          }

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


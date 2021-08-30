<template>
<div>
  <custom-nav-bar :value="vCode" @on-input="handleInput">

  </custom-nav-bar>
  <div id="user-view-main">
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
        }
      },
      methods:{
        handleInput(value){
          this.vCode = value;
          console.log(value);

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
                console.log(this.queryparamusers);
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
                  console.log(u.profileImage)

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
      },computed:{
        filteredUsers: function (){
          let queryParam = this.vCode.split(' ')
          console.log(queryParam.length)
          console.log(queryParam[1])
          console.log(queryParam[2])
          if(queryParam.length == 1) {
            return this.users.filter((user) => {
              return user.username.toLowerCase().match(queryParam[0].toLowerCase()) ||
                     user.name.toLowerCase().match(queryParam[0].toLowerCase()) ||
                    user.surname.toLowerCase().match(queryParam[0].toLowerCase());
            });
          }else if(queryParam.length == 2){
            return this.users.filter((user) => {
              return user.username.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.username.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[0].toLowerCase()) ||
                  user.name.toLowerCase().match(queryParam[1].toLowerCase()) ||
                  user.surname.toLowerCase().match(queryParam[1].toLowerCase())
                  ;
            });
          }else if(queryParam.length == 3){
            return this.users.filter((user) => {
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


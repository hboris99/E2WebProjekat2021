<template>
<div>
  <div id="user-view-main">
  <div class="row row-cols-1 row-cols-5 g-4">
    <div class="col" v-for="user in users">
      <div class="card" style="width: 10rem">
        <router-link id="link" to="/home + {{user.name}}">
        <img v-bind:src="user.profileImage" class="card-img-top h-100 w-100" alt="...">
        <div class="card-body">
          <h5 class="card-title">{{user.name + ' ' + user.surname}}</h5>
          <p class="card-text">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
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
        }
      },
      methods:{

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


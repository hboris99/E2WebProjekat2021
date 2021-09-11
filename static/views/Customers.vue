<template>
  <div>
    <custom-nav-bar>
    </custom-nav-bar>

    <div><h2>Your customers</h2></div>
    <div class="row row-cols-1 row-cols-3 g-2">
      <div class="col" v-for="customer in customers" :key="customer.username">
        <div class="card  text-white bg-dark">
          <img v-if="customer.profileImage" :src="'http://localhost:8080/image/' + customer.profileImg" class="card-img-top h-100 w-100" alt="...">
          <img v-else src="images/profile-pic-placeholder.png" class="card-img-top h-100 w-100" alt="...">        <div class="card-body">
          <h5 class="card-title">{{customer.username}}</h5>
          <p class="card-text">Ime: {{customer.name}} Surname: {{customer.surname}} Gender: {{customer.genderType }}
            Role: {{customer.userRoleType}} Points: {{customer.points}}
          </p>

        </div>
        </div>
      </div>

    </div>
  </div>

</template>

<script>
module.exports ={
  data(){
    return{
      customers: [],
    };
  },
  methods: {
    getCustomers: function (){
      if(!localStorage.jws){
        this.$router.push('/')
        return;
      }

      axios.get("/manager/users", {headers: {'Authorization': 'Bearer' + localStorage.jws}})
      .then(r=> this.customers = r.data)
      .catch(r=> console.log(r));
    }
  },
  mounted(){
    this.getCustomers();
  }
}
</script>

<style scoped>

</style>
<template>
<div>
  <custom-nav-bar>
  </custom-nav-bar>
  <div id="mainwindow">
    <h3>Dodajte novog korisnika</h3>
    <p v-if="errors.length">
      <b>Please correct the following error(s):</b>
    <ul>
      <li v-for="error in errors">{{ error }}</li>
    </ul>
    </p>
  <div id="newuserreg">

    <div class="d-flex justify-content-between">
    <form class="row g-3 needs-validation" @submit="newUser" id="sign-in-form" novalidate>
      <div class="form-floating">
        <input class="form-control" placeholder="Username" id="form-name-input" v-model="username" type="text">
        <label for="form-name-input">Username</label>
        <div class="invalid-feedback">
          Please enter your username.
        </div>
      </div>
      <div class="form-floating  mb-3">
        <input class="form-control" id="form-pass-input" v-model="password"  id="password" placeholder="Password" autocomplete="off">
        <label for="Password">password</label>
      </div>


      <div class="form-floating">
        <input class="form-control" id="fnameform" v-model="name" placeholder="First name">
        <label for="fnameform">First name</label>
      </div>
      <div class="form-floating ">
        <input class="form-control" id="lnameform" v-model="surname" placeholder="First name">
        <label for="lnameform">Last name</label>
      </div>
        <select clas="form-select form-select-lg" v-model="userRoleType">
          <option :value="null" disabled hidden>Select userRoleType</option>
          <option value="MANAGER">Manager</option>
          <option value="DELIVERER">Deliverer</option>
        </select>

        <select clas="form-select form-select-lg" v-model="gender">
          <option selected>Select gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="OTHER">Other</option>
        </select>

      <div class="form-floating input-group">
        <input type="date" v-model="birthDate" :max="new Date()">
      </div>


      <input class="btn btn-primary" type="submit" value="Create new User">

    </form>
  </div>
</div>
</div>
</div>
</template>

<script>
module.exports= {
  data: function() {
    return{
      username: '',
      password: '',
      name: '',
      surname: '',
      userRoleType: '',
      gender: '',
      birthDate: '',
      profileImage: '',
      errors: [],
    }
  },
  methods: {
    validate: function (){
      this.errors = []
      if(!this.username){
        this.errors.push('Please enter a username')
      }if(!this.password){
        this.errors.push('Please enter a password')
      }if(!this.name){
        this.errors.push('Please enter a name')
      }if(!this.surname){
        this.errors.push('Please enter a surname')
      }if(!this.gender){
        this.errors.push('Please enter a gender')
      }if(!this.birthDate){
        this.errors.push('Please enter a birth date')
      }

    },
    newUser: function (e){
        if(!localStorage.jws){
          this.$router.push('/');
          return;
        }
        this.validate()
        if(this.errors.length != 0){
          return;
        }
        e.preventDefault()
        let request = {
          username: this.username,
          password: this.password,
          name: this.name,
          surname: this.surname,
          gender: this.gender,
          birthDate: this.birthDate,
          profileImage: this.profileImage,
        }
        console.log(request)
        let url = this.userRoleType == 'MANAGER' ? '/admin/newmanager' : '/admin/newdeliverer';
        console.log(url);
        axios.post(url, request, {headers:{'Authorization': 'Bearer' + localStorage.jws}}).then(
            () => alert('User registered')).catch(r=>{
              console.log(r);
            });

    },
  },
}
</script>
<style scoped>
#mainwindow{
  display: grid;
  place-items: center;
  height: 70vh;
}
#newuserreg{
  display: grid;
  place-items: center;
  padding: 20px;
  border: 1px solid rgba(103, 5, 2, 0.67);
  border-radius: 10px;
  width: 500px;
  height: 510px
}
</style>

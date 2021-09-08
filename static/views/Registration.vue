<template>
  <div id="register-main">
    <div id="register-container">

      <form @submit="register" id="register-form">
        <b class="error">{{errors.username}}</b>
        <input v-model="username" type="text" placeholder="Username">
        <b class="error">{{errors.password}}</b>
        <input v-model="password" type="password" placeholder="Password">
        <b class="error">{{errors.firstName}}</b>
        <input v-model="firstName" type="text" placeholder="First name">
        <b class="error">{{errors.lastName}}</b>
        <input v-model="lastName" type="text" placeholder="Last name">
        <select v-model="gender">
          <option value="" disabled>Select gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="OTHER">Other</option>
        </select>
        <label>Birth date</label>
        <b class="error">{{errors.birthDate}}</b>
        <input type="date" v-model="birthDate" :max="new Date()" />
        <div class="spacer"></div>
        <input type="submit" class="button-primary" value="Register">

      </form>
    </div>
  </div>
</template>

<script>

module.exports = {
  data() {
    return {
      username: '',
      password: '',
      firstName: '',
      lastName: '',
      gender: '',
      birthDate: '',
      selected: false,
      errors: {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        birthDate: '',
      },
    };
  },
  methods: {

    validateInputs: function() {
      this.errors.username = '';
      this.errors.password = '';
      this.errors.firstName = '';
      this.errors.lastName = '';
      this.errors.birthDate = '';
      let usernameValid = !!this.username;
      let passwordValid = !!this.password ;
      let firstNameValid = !!this.firstName;
      let lastNameValid = !!this.lastName;
      let birthDateValid = !!this.birthDate && new Date(Date.parse(this.birthDate) < new Date());
      this.errors.username = usernameValid ? '' : 'Username required';
      this.errors.password = passwordValid ? '' : 'Password required';
      this.errors.firstName = firstNameValid ? '' : 'First name required';
      this.errors.lastName = lastNameValid ? '' : 'Last name required';
      this.errors.birthDate = birthDateValid ? '' : 'Select date before today';
      return usernameValid && passwordValid
          && firstNameValid && lastNameValid
          && birthDateValid;
    },
     register:async function(e) {
      e.preventDefault();
      if(!this.validateInputs()) {
        return;
      }


      let registerRequest = {
        username: this.username,
        firstName: this.firstName,
        lastName: this.lastName,
        password: this.password,
        gender: this.gender,
        birthDate:this.birthDate,
      };
      await axios.post('/user/register', registerRequest,{headers:{'Authorization': 'Bearer' + localStorage.jws}})
          .then().catch(r => console.log(r));


       let loginRequest = {
         username: this.username,
         password: this.password,
       };

       await axios.post("/user/login", loginRequest).then((r) => {
         localStorage.jws = r.data;
         this.$router.push("/");
       });
    },
  },
};
</script>

<style scoped>
#register-main {
  display: grid;
  place-items: center;
  height: 100vh;
  background:deepskyblue;
}
#register-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  background: #fff;
  padding: 20px;
  border: solid 1px #eee;
  border-radius: 10px;
}
#register-form {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-height: 40vh;
  margin-bottom: 0;
  text-align: left;
}
#signup-form button {
  margin-top: auto;
}
#signup-form button:disabled {
  background: #ddd;
}
</style>



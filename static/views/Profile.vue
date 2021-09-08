<template>
  <custom-nav-bar> </custom-nav-bar>
<div id="profile-main">
  <div id="profileData">
    <h2>{{user.username}}</h2>
    <form id="userinfo-form" @submit.prevent>
      <div id="userinfo-form-container">
        <div class="form-field">
          <label>First name</label>
          <input type="text" v-model="user.name">
        </div>
        <div class="form-field">
          <label>Last name</label>
          <input type="text" v-model="user.surname">
        </div>
        <div class="form-field">
          <label>Old password</label>
          <input type="password" v-model="oldPassword">
        </div>
        <div class="form-field">
          <label>New Password</label>
          <input type="password" v-model="newPassword">
        </div>
        <div class="form-field">
          <label>Gender</label>
          <select v-model="user.gender">
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>
        </div>
        <div class="form-field">
          <label>Birthday</label>
          <input type="date" v-model="user.dateOfBirth" :max="new Date()" />
        </div>
      </div>
      <div class="spacer"></div>
      <div class="form-field">
        <button class="button-primary" @click="updateInfo">Update info</button>
      </div>
    </form>
  </div>
</div>
</template>


<script>
  module.exports = {
    data() {
      return {
        user: {
        username: 'ppetrovic',
        password: 'ppetrovic',
        firstName: 'Petar',
        lastName: 'Petrovic',
        sex: 'MALE',
        birthDate: '',
    },
        oldPassword: 'ppetrovic',
        newPassword: '',

      }
    },
    methods: {
      updateProfile: function() {
        if(!localStorage.jws) {
        this.$router.push('/');
      return;
    }
      let file = this.$refs.file.files[0];
      let data = new FormData();
      data.append('file', file);
      let config = {
      headers: {
      'Content-Type': 'multipart/form-data', 'Authorization': 'Bearer ' + localStorage.jws,
    }
    };
      axios.post('/user/profile', data, config)
      .then(() => this.$router.go(this.$router.currentRoute))
      .catch(r => console.log(r));
    },
      getUser: function() {
        if(!localStorage.jws) {
          this.$router.push('/');
        }
        axios.get('/user', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
        .then(r => {
          this.user = r.data;
          this.oldPassword = this.user.password;
    })
      .catch(r => {
        console.log(r);
        this.$router.push('/');
    });
    },
      validateInputs: function() {
      let usernameValid = !!this.user.username;
      let oldPasswordValid = !!this.oldPassword && this.oldPassword.length >= 8;
      let newPasswordValid = !this.newPassword || this.newPassword.length >= 8;
      let firstNameValid = !!this.user.firstName;
      let lastNameValid = !!this.user.lastName;
      let birthDateValid = !!this.user.birthDate || new Date(Date.parse(this.user.birthDate)) > new Date();
      this.errors.update = '';
      this.errors.username = usernameValid ? '' : 'Username required';
      this.errors.firstName = firstNameValid ? '' : 'First name required';
      this.errors.lastName = lastNameValid ? '' : 'Last name required';
      this.errors.birthDate = birthDateValid ? '' : 'Must be before today';
      this.errors.oldPassword = oldPasswordValid ? '' : "Minimum 8 characters";
      return usernameValid && oldPasswordValid && newPasswordValid
      && firstNameValid && lastNameValid && birthDateValid;
    },
      updateInfo: function() {
      if(!this.validateInputs()) {
      return;
    }
      let data = {
      username: this.user.username,
      password: this.newPassword,
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      birthDate: this.user.birthDate,
      sex: this.user.sex,
    };
      axios.post('/user/update', data, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
      .then(() => this.$router.go(this.$router.currentRoute))
      .catch(() => this.errors.update = 'Could not update user info!');
    },
    },
      mounted() {
      this.getUser();
    },
    };
</script>

<style scoped>

</style>
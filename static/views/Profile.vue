<template>
  <div>
    <custom-nav-bar>
    </custom-nav-bar>
    <div id="profile-main">
      <div id="profile-container">
        <div id="profile-profile">
          <input type="file" ref="file" @change="updateProfile" style="display: none">
          <div v-if="!user.profileImage" class="profile-placeholder" @click="$refs.file.click()">
            +
          </div>
          <img @click="$refs.file.click()" v-else :src="'http://localhost:8080/image/' + user.profileImage" alt="profile">
          <b v-if="user.memberType">{{user.memberType}}</b>
          <b v-if="user.points != null">Points: {{user.points}}</b>
        </div>
        <div id="profile">
          <h2>Informacije o {{user.username}} korisniku</h2>
          <form id="profile-form" @submit.prevent>
            <div id="profile-form-container">
              <div class="form-field">
                <label>Name</label>
                <b class="error">{{errors.name}}</b>
                <input type="text" v-model="user.name">
              </div>
              <div class="form-field">
                <label>Surname</label>
                <b class="error">{{errors.surname}}</b>
                <input type="text" v-model="user.surname">
              </div>
              <div class="form-field">
                <label>Old password</label>
                <b class="error">{{errors.oldPassword}}</b>
                <input type="password" v-model="oldPassword">
              </div>
              <div class="form-field">
                <label>New Password</label>
                <b class="error">{{errors.newPassword}}</b>
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
                <b class="error">{{errors.birthDate}}</b>
                <input type="date" v-model="user.birthDate" :max="new Date()" />
              </div>
            </div>
            <div class="spacer"></div>
            <div class="form-field" id="updateButton">
              <b class="error">{{errors.update}}</b>
              <button class="button-primary"  @click="updateInfo">Update info</button>
            </div>
          </form>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
module.exports = {
  data() {
    return {
      user: {
        username: '',
        password: '',
        name: '',
        surname: '',
        gender: 'MALE',
        birthDate: '',
      },
      oldPassword: '',
      newPassword: '',
      errors: {
        username: '',
        oldPassword: '',
        newPassword: '',
        name: '',
        surname: '',
        update: '',
        birthDate: '',
      },
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
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer ' + localStorage.jws,
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
      let oldPasswordValid = !!this.oldPassword && this.oldPassword.length >= 4;
      let newPasswordValid = !this.newPassword || this.newPassword.length >= 4;
      let nameValid = !!this.user.name;
      let surnameValid = !!this.user.surname;
      let birthDateValid = !!this.user.birthDate || new Date(Date.parse(this.user.birthDate)) > new Date();
      this.errors.update = '';
      this.errors.username = usernameValid ? '' : 'Username required';
      this.errors.name = nameValid ? '' : 'First name required';
      this.errors.surname = surnameValid ? '' : 'Last name required';
      this.errors.birthDate = birthDateValid ? '' : 'Must be before today';
      this.errors.oldPassword = oldPasswordValid ? '' : "Minimum 8 characters";
      return usernameValid && oldPasswordValid && newPasswordValid
          && nameValid && surnameValid && birthDateValid;
    },
    updateInfo: function() {
      if(!this.validateInputs()) {
        return;
      }
      let data = {
        username: this.user.username,
        password: this.newPassword,
        name: this.user.name,
        surname: this.user.surname,
        birthDate: this.user.birthDate,
        gender: this.user.gender,
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
#profile-main {
  display: grid;
  place-items: center;
  height: 92vh;
  background: rgba(60,136,240,0.74) ;
}
#profile-container {
  display: grid;
  grid-template-columns: 1fr auto;
  background: #fff;
  border: solid 1px rgba(132,132,132,0.59) ;
  padding: 40px;
  min-height: 70vh;
  gap: 20px;
}
#profile-profile {
  display: flex;
  flex-direction: column;
}
#profile-profile img {
  cursor: pointer;
  width: 128px;
  height: 128px;
  margin-bottom: 20px;
}
#profile {
  display: flex;
  flex-direction: column;
}
#profile-form {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
}
#updateButton{
  padding: 30px;
}
#profile-form-container {
  display: grid;
  gap: 40px;
  grid-template-columns: 1fr 1fr;
}
.form-field {
  display: flex;
  flex-direction: column;
}
.profile-placeholder {
  display: grid;
  place-items: center;
  font-weight: 500;
  font-size: 5rem;
  width: 128px;
  height: 128px;
  cursor: pointer;
  background: #eeeeee;
}
</style>
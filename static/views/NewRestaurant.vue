<template>
<div>
  <custom-nav-bar></custom-nav-bar>
  <div id="mainwindow">
    <h3>Dodajte novi restoran </h3>
    <p v-if="errors.length">
      <b>Please correct the following error(s):</b>
    <ul>
      <li v-for="error in errors">{{ error }}</li>
    </ul>
    </p>
    <div id="newuserreg">
  <div class="d-flex justify-content-between ">
    <form class="row g-3 " @submit="newRestaurant" id="sign-in-form" novalidate>
      <div class="form-floating">
        <input class="form-control" placeholder="Restaurant Name" id="form-name-input" v-model="restaurantname" type="text">
        <label for="form-name-input">Name</label>

      </div>




      <select class="form-select form-select" v-model="selectedType">
        <option value="" disabled>Select restaurant Type</option>
        <option v-for="type in types" :key="type" :value="type">{{type}}</option>

      </select>
      <select class="form-select form-select" v-model="selectedManager">
        <option value="" disabled>Select manager</option>
        <option value="New">New</option>
        <option v-for="manager in managers" :key="manager.username" :value="manager.username">{{manager.username}}</option>
      </select>
      <geosearch @selected="selectLocation" >

      </geosearch>

      <div v-if="selectedLocation">
        <div class="form-floating">
          <input class="form-control"  id="form-place-input" v-model="place" type="text">
          <label for="form-place-input">State</label>
        </div>
        <div class="form-floating">
          <input class="form-control"  id="form-street-input" v-model="streetAndNumber" type="text">
          <label for="form-street-input">Street and Number</label>
        </div>
        <div class="form-floating">
          <input class="form-control"  id="form-zip-input" v-model="zipcode" type="text">
          <label for="form-zip-input">Zip Code</label>
        </div>
      </div>

      <div class="form-floating form-group">
        <input style="display: none" type="file" ref="file" @change="selectFile">
        <div v-if="!file" id="add-thumbnail" @click="$refs.file.click()">
          +
        </div>
        <img v-else :src="fileUrl" alt="rest img"/>

      </div>


      <input class="btn btn-primary" type="submit" value="Create new Restaurant">

    </form>
    <div id="spacer"></div>
    <div class="d-flex justify-content-between">

    <form @submit.prevent :class="selectedManager != 'row g-3 ' ? 'row g-3 ' : ''">
      <div class="form-floating form-group">
        <input class="form-control"  id="form-username-input" v-model="username" type="text">
        <label for="form-username-input">Username</label>
      </div>
      <div class="form-floating form-group">
        <input class="form-control"  id="form-password-input" v-model="password" type="text">
        <label for="form-password-input">Password</label>
      </div>
      <div class="form-floating form-group">
        <input class="form-control"  id="form-fname-input" v-model="name" type="text">
        <label for="form-fname-input">First Name</label>
      </div>
      <div class="form-floating form-group">
        <input class="form-control"  id="form-lname-input" v-model="surname" type="text">
        <label for="form-lname-input">Surname</label>
      </div>
      <select class="form-select form-select-lg form-floating" v-model="sex" :disabled="selectedManager != 'New'">
        <option value="MALE">Male</option>
        <option value="FEMALE">Female</option>
        <option value="OTHER">Other</option>
      </select>
      <div class="form-floating input-group">
        <input type="date" v-model="birthDate" :max="new Date()">
      </div>
      <button
          :class="selectedManager != 'New' ? 'btn btn-primary disabled' : 'btn btn-primary'"
          :disabled="selectedManager != 'New'"
          @click="registerManager">Add manager</button>
    </form>
  </div>
  </div>
</div>
  </div></div>
</template>

<script>
module.exports= {
  data: function (){
    return {
      restaurantname:'',
      zipcode:'',
      streetAndNumber:'',
      place:'',
      selectedManager: '',
      managers: [],
      selectedType: '',
      types: ['Italian', 'Chinese', 'Mexican', 'FastFood', 'Grill'],
      selectedLocation: false,
      file: '',
      fileUrl: '',
      lat: '',
      lon: '',
      username: '',
      password: '',
      name: '',
      surname: '',
      sex: 'MALE',
      birthDate: '',
      errors:[],
    };
  },methods: {
    selectFile: function (){
      let file = this.$refs.file.files[0]
      this.fileUrl = URL.createObjectURL(file);
      this.file = file;
    },
    selectLocation: function(l) {
      console.log(this.type)
      console.log('Radi console log')
      console.log(l.lat);
      console.log(l.address.city)
      this.place = l.address.city;
      this.streetAndNumber = l.address.road +  l.address.house_number;
      this.zipcode = l.address.postcode;
      this.lat = l.lat;
      this.lon = l.lon;
      this.selectedLocation = true;

    },
    validateManager: function (){
      this.errors =[];
      if(!this.username){
        this.errors.push("Unesite ime username")
      } if(!this.password){
        this.errors.push("Unesite password")
      } if(!this.sex){
        this.errors.push("Unesite sex!")
      } if(!this.surname){
        this.errors.push("Unesite surname")
      }if(!this.name){
        this.errors.push("Unesite name")
      }
    },
    validateRest:function (){
      this.errors =[];
      if(!this.restaurantname){
        this.errors.push("Unesite ime restorana")
      } if(!this.selectedType){
        this.errors.push("Unesite tip")
      } if(!this.selectedManager){
        this.errors.push("Unesite menadzera!")
      } if(!this.file){
        this.errors.push("Unesite logo")
      }

        },
    newRestaurant: function (e){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      e.preventDefault()
      this.validateRest();
      if(this.errors.length != 0){
        return;
      }
      console.log(this.place)
      let data = new FormData();
      let req ={
        name: this.restaurantname,
        restaurantType: this.selectedType,
        managerUsername: this.selectedManager,
        location: {
          latitude: this.lat,
          longitude: this.lon,
          adress: {
            place: this.place,
            streetAndNumber: this.streetAndNumber,
            zipcode: this.zipcode,

          }
        }
      };
      data.append('req', JSON.stringify(req));
      data.append('file', this.file);

      let config = {
        headers:{
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer' + localStorage.jws,
        },
      };

      axios.post('/admin/restaurant', data, config)
      .then(() => {
        this.$router.go(0);
      }).catch(r => console.log(r));

    },
    getManagers: function (e){
      if(!localStorage.jws){
        this.$router.push('/')
        return
      }

      axios.get('/admin/getmanagers', {headers:{'Authorization': 'Bearer' + localStorage.jws}}).then(r =>
          this.managers = r.data
      ).catch(r => console.log(r));
    },
    registerManager: function(e) {

      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      e.preventDefault()
      this.validateManager();
      if(this.errors.length != 0){
        return
      }
      let registerRequest = {
        username: this.username,
        password: this.password,
        name: this.name,
        surname: this.surname,
        gender: this.sex,
        birthDate: this.birthDate,
      };
      console.log(registerRequest);
      axios.post('/admin/newmanager', registerRequest, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => {
            this.getManagers();
            })
          .catch((r) => console.log(r));
    },
  },
  mounted() {
    this.getManagers();
  }
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
  border-radius: 15px;
  width: 570px;
  height: 510px
}
img{
  width: 150px;
  length: 150px;
}
#spacer{
  width: 150px;
}
</style>

<template>
<div>
  <custom-nav-bar></custom-nav-bar>
  <div id="mainwindow"><div id="newuserreg">
  <div class="d-flex justify-content-between">
    <form class="row g-3 needs-validation" @submit="newRestaurant" id="sign-in-form" novalidate>
      <div class="form-floating">
        <input class="form-control" placeholder="Restaurant Name" id="form-name-input" v-model="name" type="text">
        <label for="form-name-input">Name</label>

      </div>




      <select clas="form-select form-select-lg" v-model="restaurantType">
        <option :value="null" disabled hidden>Select restaurant Type</option>
        <option value="ITALIAN">Italian</option>
        <option value="MEXICAN">MEXICAN</option>
        <option value="CHINESE">Chinese</option>
        <option value="FASTFOOD">Fast Food</option>
        <option value="GRILL">Grill</option>

      </select>
      <select clas="form-select form-select-lg" v-model="selectedManager">
        <option value="" disabled>Select manager</option>
        <option value="New">New</option>
        <option v-for="manager in managers" :key="manager.username" :value="manager.username">{{manager.username}}</option>
      </select>
      <geosearch @selected="selectLocation">

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
  </div>
</div>
  </div></div>
</template>

<script>
module.exports= {
  data: function (){
    return {
      name:'',
      zipcode:'',
      streetAndNumber:'',
      place:'',
      selectedManager: '',
      managers: [],
      restaurantType: '',
      selectedLocation: false,
      file: '',
      fileUrl: '',
      lat: '',
      lon: '',
    };
  },methods: {
    selectFile: function (){
      let file = this.$refs.file.files[0]
      this.fileUrl = URL.createObjectURL(file);
      this.file = file;
    },
    selectLocation: function(l) {
      console.log('Radi console log')
      console.log(l.address.city)
      this.place = l.address.city;
      this.streetAndNumber = l.address.road +  l.address.house_number;
      this.zipcode = l.address.postcode;
      this.lat = l.lat;
      this.lon = l.lon;
      this.selectedLocation = true;

    },
    newRestaurant: function (){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      console.log(this.place)
      let data = new FormData();
      let req ={
        name: this.name,
        restaurantType: this.restaurantType,
        managerUsername: this.selectedManager,
        location: {
          lat: this.lat,
          lon: this.lon,
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
    getManagers: function (){
      if(!localStorage.jws){
        this.$router.push('/')
        return
      }
      axios.get('/admin/getmanagers', {headers:{'Authorization': 'Bearer' + localStorage.jws}}).then(r =>
          this.managers = r.data
      ).catch(r => console.log(r));
    }
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
  height: 100vh;
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
img{
  width: 150px;
  length: 150px;
}
</style>

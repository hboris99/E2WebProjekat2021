<template>
<div>
    <custom-nav-bar :value="vCode" @on-input="handleInput"></custom-nav-bar>

      <div class="container">
          <div class="row g-5 align-items-end">
          <div class="col">
              <select  v-model="selectedType">
                <option value="">Select type</option>
                <option v-for="type in types" :key="type" :value="type">{{type}}</option>
              </select>
             </div>
          <div class="col ">
            <select v-model="selectedAvailability">
              <option value="">OPEN/Close checklist</option>
              <option v-for="work in working" :key="work" :value="work">{{work}}</option>
            </select>
          </div>

          <div class="col ">
            <p @click="sort('name')">Name<span v-if="sortBy == 'name'" v-html="sortSymbol"></span></p>
          </div>
          <div class="col ">
            <p @click="sort('location')">Location<span v-if="sortBy == 'location'" v-html="sortSymbol"></span></p>
          </div>
          <div class="col ">
            <p @click="sort('avgPoints')">Average Review<span v-if="sortBy == 'points'" v-html="sortSymbol"></span></p>
          </div>
          </div>

        <div class="row row-cols-1 row-cols-3 g-2">
          <div class="col" v-for="restaurant in filteredRestaurants">
            <div  class="card  text-white bg-dark">
              <img v-if="restaurant.logo" :src="'http://localhost:8080/image/' + restaurant.logo" class="card-img-top h-100 w-100" alt="...">
              <img v-else src="images/restaurant.png" class="card-img-top h-100 w-100" alt="...">
              <div class="card-body">
                <h5 class="card-title">{{restaurant.name}}</h5>
                <p class="card-text">
                  Type of restaurant: {{restaurant.restaurantType}}
                  <br>
                  Location: {{restaurant.restaurantLocation }}
                  Average review: {{restaurant.avgPoints}}
                </p>
                <div >
                <button   class="btn btn-secondary">More details</button>
              </div>
            </div>
            </div>
          </div>
        </div>
      </div>
</div>
</template>

<script>
module.exports=
    {
      data: function (){
        return{
          restaurants:[],
          logo: '',
          vCode:'',
          selectedType: '',
          selectedAvailability: '',
          sortBy: 'working',
          sortDirection:'asc',
          types: ["Italian","Chinese","Mexican","FastFood" ,"Grill"],
          working: [0 , 1]
        }
      },
      methods:{
        handleInput(value){
          this.vCode = value;

        },
        sort: function(param){
          if(this.sortBy == param) {
            this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
          }
          this.sortBy = param;
          console.log(this.sortBy)
          let sortedRestaurants = this.restaurants;
          this.restaurants.sort((a, b) => {
            let aval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, a);

            let bval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, b);


            let modifier = 1;
            if(this.sortDirection == 'desc') {
              modifier = -1;
            }
            if(aval < bval) {
              return -1 * modifier;
            }
            if(aval > bval) {
              return modifier;
            }
            return 0;
          });
        },
        getFilteredByType : function (restaurants,param){
          if(!param){
            return restaurants;
          }
          return restaurants.filter(restaurant=>restaurant.restaurantType.toLowerCase().indexOf(param.toLowerCase()) >-1);
        },
        getFilteredByAvail: function (restaurants,param){
          if(!param){
            return restaurants;
          }
          return restaurants.filter(restaurant => restaurant.working == param);
        },
        getRestaurants: function (){
          this.restaurants = []
          axios.get('/getRestaurants', {headers: {'Authorization': 'Bearer' + localStorage.jws}})
              .then(r=>
                  {
                    let restaurants = r.data;
                    restaurants.forEach(res =>
                    {

                    });
                    this.restaurants=[...restaurants];
                  }
              ).catch(r =>console.log(r));
        },
          },
        computed :{
          filteredRestaurants: function (){
            return this.getFilteredByType(this.getFilteredByAvail(this.restaurants,this.selectedAvailability),this.selectedType);
          },
          sortSymbol: function() {
            return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
          },
        },
        mounted(){
          this.getRestaurants();
        },

    };
</script>

<style scoped>

</style>
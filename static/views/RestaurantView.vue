<template>
<div>

  <custom-nav-bar></custom-nav-bar>
  <div class="container">
    <div id="restaurantInfo">
  <img :src="'http://localhost:8080/image/' + restaurant.logo">
  <div>


    <h2>{{restaurant.name}}</h2>
    <b :class="restaurant.working == true ? 'open' : 'closed'">{{restaurant.working}}</b></div>
      <button v-if="isRegular && restaurant.working == true"><router-link :to="'/order/' + restaurant.name ">Place an order!</router-link></button>
    </div>
    <div id="location">
      <h2>Where you can find us</h2>
      <div id="location-container">
        <div id="location-info">
          <label>State</label>
          <p>{{restaurant.restaurantLocation.adress.place}}</p>
          <label>City</label>
          <p>{{restaurant.restaurantLocation.adress.streetAndNumber}}</p>
          <label>Street</label>
          <p>{{restaurant.restaurantLocation.adress.zipcode}}</p>
        </div>
        <div id="mapElement" class="mapElement"></div>
      </div>
    </div>
    <div class="row row-cols-1 row-cols-3 g-2" >

    <div v-for="article in restaurant.articleList">

  <div v-if="article.articleType == 'Food' && article.deleted == false" class="card  text-white bg-primary">
    <img  :src="'http://localhost:8080/image/' + article.logo" class="card-img-top h-100 w-100" alt="...">
    <div class="card-body">
      <h5 class="card-title">{{article.name}}</h5>
      <p class="card-text">Price: {{article.price}} Type: {{article.articleType}} Quanityt: {{article.quantity }}
        Description: {{article.description}}
      </p>
      <div>


      </div>
    </div>
  </div>
  <div v-if="article.articleType == 'Drink' && article.deleted == false" class="card  text-white bg-secondary">
    <img  :src="'http://localhost:8080/image/' + article.logo" class="card-img-top h-100 w-100" alt="...">
    <div class="card-body">
      <h5 class="card-title">{{article.name}}</h5>
      <p class="card-text">Price: {{article.price}} Type: {{article.articleType}} Quanityt: {{article.quantity }}
        Description: {{article.description}}
      </p>


    </div>
    </div>
  </div>
    </div>
  </div>
</div>
</template>

<script>
module.exports = {
  data: function() {
      return{
        restaurantName: '',
        restaurant: '',
        articles:[],
        map: null,
        isAdmin:'',
        isManager: '',
        isRegular: '',

      }
  },methods:{

    getRestaurant: async function() {
      let name = this.$route.params.name;
      return axios.get('/restaurant/' + name);
    },
    getRole: function() {
      if(!localStorage.jws) {
        return;
      }
      axios.get('/user/role', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => {
            if(r.data == 'Admin') {
              this.isAdmin = true;
            }
            if(r.data == 'Manager') {
              if(!localStorage.jws) {
                return;
              }
              this.isManager = true;
            }
            if(r.data == 'Buyer') {
              this.isRegular = true;
              this.isManager = false;
              this.isAdmin = false;
            }
          })
    },

  },

  mounted(){
    this.getRole();
    this.getRestaurant().then(r  => {
      this.restaurant = r.data;
      this.articles = this.restaurant.articleList;

      console.log(this.restaurant)
      console.log(this.articles)
      this.map = new ol.Map({
        target: 'mapElement',
        layers: [
          new ol.layer.Tile({
            source: new ol.source.OSM()
          })
        ],
        view: new ol.View({
          center: ol.proj.fromLonLat([this.restaurant.restaurantLocation.longitude, this.restaurant.restaurantLocation.latitude]),
          zoom: 15
        })
      });
      console.log(this.map);
      let restPointer = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([this.restaurant.restaurantLocation.longitude, this.restaurant.restaurantLocation.latitude]))
      });
      restPointer.setStyle(
          new ol.style.Style({
            image: new ol.style.Icon({
              crossOrigin: 'anonymous',
              src: 'img/pointer.png',
              scale: 0.02,
            }),
          })
      );
      var layer = new ol.layer.Vector({
        source: new ol.source.Vector({
          features: [ restPointer,
          ]})});
      this.map.addLayer(layer);
      console.log(layer);
      console.log(this.map);

    }).catch(r => console.log(r));

  },

}
</script>

<style scoped>
#location {
  width: 80%;
  margin: 0 auto;
}
#location h2 {
  color: #666;
  border-bottom: solid 1px #eee;
}
#location-container {
  display: grid;
  grid-template-columns: 1fr auto;
  padding: 20px;
}
.mapElement {
  width: 400px;
  height: 400px;
}
#textBox{
  width: 200px;
}
img{
  width: 150px;
  height: 150px;
}
</style>
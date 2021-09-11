<template>
<div>

  <custom-nav-bar></custom-nav-bar>
  <div class="container">
    <div id="restaurantInfo">
  <img :src="'http://localhost:8080/image/' + restaurant.logo">
  <div>


    <h2>{{restaurant.name}}</h2>
    <b >Our average rating: {{restaurant.avgPoints}}</b></div>
      <button class="btn btn-primary" v-if="isBuyer && restaurant.working == true"><router-link id="link" :to="'/order/' + restaurant.name ">Place an order!</router-link></button>
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
    <div id="comments">
      <div class="comments">
        <div v-for="comment in comments" :key="comment.id" class="comment">
          <div class="comment-container"> <div class="comment-info">
            <h3>{{comment.username}}</h3>
            <b>{{comment.rating}}/5.0</b>
            <b v-if="isManager" :class="comment.status">{{comment.status}}</b>
            <p>{{comment.content}}</p>
          </div>
            <div class="spacer"></div>
            <img :src="'http://localhost:8080/image/' + comment.userImage" alt="commenter pic">
          </div>
          <div v-if="(isManager || isAdmin)" class="comment-actions">
            <button class="button-approve" v-if="isManager && comment.status == 'Pending'" @click="approveComment(comment.id)">Approve</button>
            <div class="spacer"></div>
            <button class="button-deny" v-if="isManager && comment.status == 'Pending'" @click="declineComment(comment.id)">Deny</button>
            <button class="button-deny" v-if="isAdmin" @click="deleteComment(comment.id)">Delete</button>
          </div>
        </div>
      </div>
      <div v-if="allowedToComment" id="review-container">
        <h3>Share your thoughts</h3>
        <label>Rating: </label>
        <b>{{review.rating}} / 5</b>
        <input type="range" min="1" max="5" v-model="review.rating"/>
        <textarea view="review.body" cols="30" rows="10" v-model="review.content"></textarea>
        <button @click="postComment">Add review</button>
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
        restaurant: {
          name: "Liman",
          status: 'Open',
          type: 'Pizzeria',
          avgScore: 4.3,
          restaurantLocation: {
            lat: 45.2408613,
            lon: 19.8360667,
            adress:{
              place: "Novi Sad",
              streetAndNumber: "Sekspirova12a",
              zipcode: 21010,
              }
          }
        },
        articles:[],
        comments: [],
        map: null,
        allowedToComment: false,
        isManager: false,
        isAdmin: false,
        review: {
          rating: '',
          content:'',
        },
        isManager: false,
        isAdmin: false,
        isBuyer: false,


      }
  },methods:{
    approveComment: function(id) {
      if(!localStorage.jws) {
        this.$route.push('/');
        return;
      }
      axios.post('/manager/comment/approve', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => this.$router.go(0))
          .catch(r => console.log(r));
    },
    declineComment: function(id) {
      if(!localStorage.jws) {
        this.$route.push('/');
        return;
      }
      axios.post('/manager/comment/decline', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => this.$router.go(0))
          .catch(r => console.log(r));
    },
    deleteComment: function(id) {
      if(!localStorage.jws) {
        this.$route.push('/');
        return;
      }
      axios.delete('/admin/comment/' + id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => this.$router.go(0))
          .catch(r => console.log(r));
    },
    getOwnerComments: function() {
      if(!localStorage.jws) {
        this.$route.push('/');
        return;
      }
      axios.get('/owner/' + this.$route.params.name + '/comment', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => this.comments.push(...r.data))
          .catch(r => console.log(r));
    },
    getComments: function() {
      axios.get('/restaurant/' + this.$route.params.name + '/comment')
          .then(r => {
            {
              this.comments = [...r.data]

            }
          })
          .catch(r => console.log(r));
    },
    postComment: function() {
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      axios.post('/user/comment/' + this.$route.params.name, this.review, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => this.$router.go(0))
          .catch(r => console.log(r));
    },

    canComment: function() {
      if(!localStorage.jws) {
        return;
      }
      axios.get('/user/cancomment/' + this.$route.params.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => {
            this.allowedToComment = true;
          })
          .catch(() => {
            this.allowedToComment = false;
          });
    },

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
              this.getOwnerComments();
            }
            if(r.data == 'Manager') {
              if(!localStorage.jws) {
                return;
              }
              axios.get('/manager/isowner/' + this.$route.params.name, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                  .then(() => {
                    this.isManager = true;
                    this.getOwnerComments();
                  });

            }
            if(r.data == 'Buyer') {
              this.isBuyer = true;
              this.isManager = false;
              this.isAdmin = false;
            }
          })
    },createMap: function (){
      this.map = new ol.Map({
        target: 'map',
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
      let restPointer = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([this.restaurant.restaurantLocation.longitude, this.restaurant.restaurantLocation.latitude]))
      });
      restPointer.setStyle(
          new ol.style.Style({
            image: new ol.style.Icon({
              crossOrigin: 'anonymous',
              src: 'images/pointer.png',
              scale: 0.02,
            }),
          })
      );
      var layer = new ol.layer.Vector({
        source: new ol.source.Vector({
          features: [ restPointer,
          ]})});
      this.map.addLayer(layer);
    },

  },

  mounted(){

    this.getComments();
    this.canComment();
    this.getRole();
    this.getRestaurant().then(r  => {
      this.restaurant = r.data;
      this.articles = this.restaurant.articleList;





    }).catch(r => console.log(r));
    this.$nextTick(function () {
      this.createMap();
    })
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
}#location {
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
#comments h2 {
  color: #666;
  border-bottom: solid 1px #eee;
}
.comments {
  height: 400px;
  overflow: auto;
}
.comment-container {
  display: flex;
  flex-direction: row;
  /* width: 400px; */
}
.comment {
  border-bottom: solid 1px #eee;
  padding: 5px;
}
.comment-container img {
  width: 128px;
  height: 128px;
}
.comment-info {
  display: flex;
  flex-direction: column;
}
.comment-info b {
  text-transform: uppercase;
}
.comment-info h3 {
  font-size: 2.5rem;
}
.comment-actions {
  display: flex;
  flex-direction: row;
}
.approved {
  color: #2ecc71;
}
.button-approve {
  color: #fff;
  background-color: #2ecc71;
}
.button-deny {
  color: #fff;
  background-color: #e74c3c;
}
.pending {
  color: #666;
}
.denied {
  color: #e74c3c;
}
#link{
  color: white;
  text-decoration-line: none;
}
#review-container {
  display: flex;
  flex-direction: column;
}
#review-container h3 {
  margin-top: 20px;
  font-size: 2.5rem;
  color: #666;
}
#restaurantInfo{
  position: center;
  }
</style>
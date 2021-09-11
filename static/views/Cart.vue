<template>
  <div>
<custom-nav-bar>

</custom-nav-bar>
    <div id="Cart view">
      <h1>Your cart</h1>
      <div id="articles">
        <div class="row row-cols-1 row-cols-3 g-2">
          <div class="col" v-for="ci in cart.cartItemsList">
            <div  class="card  text-white bg-dark">
              <img :src="'http://localhost:8080/image/' + ci.article.logo" class="card-img-top h-100 w-100" alt="...">
              <h5 class="card-title">{{ci.article.name}}</h5>
              <p class="card-text">Kolicina: {{ci.article.quantity}} <span v-if="ci.article.articleType == 'FOOD'">g</span><span v-else>ml</span>
                Price of item: {{ci.article.price}}
                Ammount : {{ci.quantity }}
                Price: {{ci.price}} din
              </p>
              <div class="btn-group" role="group">
                <button  @click="deleteArticle(ci)" class="btn btn-secondary">Delete</button>

              </div>
            </div>
            </div>
      </div>
        <div>
          <b>Your total is: {{cart.price}}</b>

          <button class="btn btn-primary" :disabled="cart.cartItemsList.length <= 0" @click="createOrder"> Order</button>
        </div>
    </div>

  </div>
  </div>
</template>

<script>
module.exports = {
  data(){
    return{
    cart: null,
    }
  }, methods: {
    createOrder: function (){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }

      axios.post('/user/order', {}, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(() => this.$router.push('/orders'))
          .catch(r => {
            console.log(r);
          });
    },
    deleteArticle: function(ci){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      axios.delete('user/cart/delete/'  + ci.article.name, {headers: {'Authorization': 'Bearer' + localStorage.jws}})
      .then(() => this.getCart())
      .catch(r => console.log(r));
    },
      getCart: function (){
        if(!localStorage.jws) {
          this.$router.push('/');
          return;
        }
        axios.get('/user/cart', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
        .then(r => {
         this.cart = r.data;
         console.log(this.cart)
        }).catch(r => console.log(r));
      },
  }, mounted(){
    this.getCart();
  }
}
</script>

<style scoped>

</style>
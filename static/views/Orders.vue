<template>
  <div>
    <custom-nav-bar>
    </custom-nav-bar>

    <div id="Main Order Area">
      <div id="searchPart">
          <input v-if="role != 'Manager'" type="text" placeholder="Restaurant" v-model="restaurantName" @keydown.enter="getOrders">
        <div id="sortPart">
          <p @click="sort('restaurant.name')">Restaurant<span v-if="sortBy == 'restaurant.name'" v-html="sortSymbol"></span></p>
          <p @click="sort('date')">Date<span v-if="sortBy == 'date'" v-html="sortSymbol"></span></p>
          <p @click="sort('price')">Price<span v-if="sortBy == 'price'" v-html="sortSymbol"></span></p>
        </div>
        <div id="filters-container">
          <input type="number" min="0" v-model="fromPrice" placeholder="Min price" @keydown.enter="getOrders">
          <input type="number" min="0" v-model="toPrice" placeholder="Max price" @keydown.enter="getOrders">
          <div class="filter-item">
            <label>From date:</label>
            <input type="date" v-model="fromDate" @keydown.enter="getOrders">
          </div>
          <div class="filter-item">
            <label>To date:</label>
            <input type="date" v-model="toDate" @keydown.enter="getOrders">
          </div>
          <select v-model="selectedType">
            <option value="">Select type</option>
            <option v-for="type in types" :key="type" :value="type">{{type}}</option>
          </select>
          <select v-model="selectedStatus" >
            <option value="">Select status</option>
            <option v-for="status in statuses" :key="status" :value="status">{{status}}</option>
          </select>
          <div class="filter-item">
            <label>Undelivered: </label>
            <input type="checkbox" v-model="undelivered" />
          </div>
        </div>
      </div>
      </div>
      <div class="row row-cols-1 row-cols-3 g-2">
        <div class="col" v-for="result in sortedResults" :key="result.id">
          <div class="card  text-white bg-dark">
            <div class="card-body">
            <h5 class="card-title">{{result.id}} {{result.date}} {{result.status}}</h5>
            <p class="card-text">Restaurant: {{result.restaurant.name}} ({{result.restaurant.restaurantType}})
              Adress: {{result.restaurant.restaurantLocation.adress.place}}, {{result.restaurant.restaurantLocation.adress.streetAndNumber}}, {{result.restaurant.restaurantLocation.adress.zipcode}}
            </p>
            <div class="btn-group" role="group">
              <button  v-if="role == 'Buyer' && result.stats == 'Pending'" @click="cancelOrder(result.id)" class="btn btn-secondary">Cancel</button>
              <button  v-if="role == 'Manager' && result.stats == 'Pending'" @click="prepareOrder(result.id)" class="btn btn-secondary">Prepare</button>
              <button  v-if="role == 'Manager' && result.stats == 'Preparing'" @click="waitOrder(result.id)" class="btn btn-secondary">Wait</button>
              <button  v-else-if="role == 'Deliverer' && result.stats == 'Waiting'" @click="requestOrder(result.id)" class="btn btn-secondary">Request</button>
              <button  v-else-if="role == 'Deliverer' && result.stats == 'Transport'" @click="deliverOrder(result.id)" class="btn btn-secondary">Deliver</button>

            </div>
          </div>
          </div>
    </div>

    </div>
    </div>
</template>

<script>
module.exports = {
  data() {
    return{
      role: '',
      undelivered: false,
      selectedStatus: '',
      selectedType: '',
      fromDate: '',
      toDate: '',
      fromPrice: '',
      toPrice: '',
      restaurantName: '',
      sortBy: 'restaurant.name',
      sortDirection: 'asc',
      results: [],
      types: ['Italian', 'Chinese', 'Mexican', 'FastFood','Grill',],
      statuses: ['Pending', 'Preparing', 'Waiting', 'Transport', 'Delivered', 'Canceled',],
    }
  },
  methods: {
    cancelOrder: function (id){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }

      axios.delete('/user/order/' + id,{headers: {'Authorization': 'Bearer ' + localStorage.jws}} )
      .then(() => this.getOrders()).catch(r => console.log(r));
    },
    waitOrder: function (id){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      axios.put('/manager/order/wait', id,{headers: {'Authorization': 'Bearer ' + localStorage.jws}} )
          .then(() => this.getOrders()).catch(r => console.log(r));
    },

    requestOrder: function (id){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;

      }
      axios.put('/deliverer/request', id,{headers: {'Authorization': 'Bearer' + localStorage.jws}} )
          .then(() => this.getOrders()).catch(r => console.log(r));
    },
    deliverOrder: function (id){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;

      }
      axios.put('/deliverer/deliver', id,{headers: {'Authorization': 'Bearer' + localStorage.jws}} )
          .then(() => this.getOrders()).catch(r => console.log(r));
    },
    prepareOrder: function (id){
      if(!localStorage.jws) {
        this.$router.push('/');
        return;

      }
      axios.put('/manager/order/prepare', id,{headers: {'Authorization': 'Bearer' + localStorage.jws}} )
          .then(() => this.getOrders()).catch(r => console.log(r));
    },
    getRole: function () {
      if (!localStorage.jws) {
        return;
      }
      axios.get('/user/role', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => this.role = r.data);

    }, getOrders: function () {
      if (!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      let priceFrom = this.fromPrice ? '&pricefrom=' + this.fromPrice : '';
      let priceTo = this.toPrice ? '&priceto=' + this.toPrice : '';
      let dateFrom = this.fromDate ? '&datefrom=' + this.fromDate : '';
      let dateTo = this.toDate ? '&dateto=' + this.toDate : '';
      let query = '?rest=' + this.restaurantName + priceFrom + priceTo + dateFrom + dateTo;
      axios.get('/user/order' + query, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => {
            console.log(r.data);
            this.results = r.data;
          })
          .catch(r => console.log(r));
    },
    sort: function (s) {
      if (this.sortBy == s) {
        this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
      }
      this.sortBy = s;
    },
    },
  computed: {
    sortedResults: function () {
      let res = [...this.results];
      res.sort((a, b) => {
        let aval = this.sortBy.split('.').reduce(function (p, prop) {
          return p[prop];
        }, a);
        let bval = this.sortBy.split('.').reduce(function (p, prop) {
          return p[prop];
        }, b);
        let modifier = 1;
        if (this.sortDirection == 'desc') {
          modifier = -1;
        }
        if (aval < bval) {
          return -1 * modifier;
        }
        if (aval > bval) {
          return modifier;
        }
        return 0;
      });
      if (this.selectedType) {
        res = res.filter(r => r.restaurant.restaurantType == this.selectedType);
      }
      if (this.selectedStatus) {
        res = res.filter(r => r.stats == this.selectedStatus);
      }
      if (this.undelivered) {
        res = res.filter(r => r.stats != 'Delivered');
      }
      console.log(res);
      return res;
    },
    sortSymbol: function () {
      return this.sortDirection == 'asc' ? '&#x25B2;' : '&#x25BC;'
    },
  },
  mounted(){
    this.getRole();
    this.getOrders();
  }
}
</script>

<style scoped>

</style>
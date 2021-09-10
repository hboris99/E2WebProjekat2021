<template>
  <div>
    <custom-nav-bar>

    </custom-nav-bar>
    <div><h2>Delivery requests</h2></div>
    <div class="row row-cols-1 row-cols-3 g-2">
      <div class="col" v-for="request in requests" :key="requests.orderId">
        <div class="card  text-white bg-dark">
          <div class="card-body">
            <h5 class="card-title">{{request.orderId}} {{request.username}}</h5>
            <p class="card-text">Do you want to accept or decline this deliverer?
            </p>
            <div class="btn-group" role="group">
              <button  @click="accept(request)" class="btn btn-secondary">Accept</button>
              <button @click="decline(request)" class="btn btn-danger">Decline</button>
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
    return {
      requests: [],
    };
  },
  methods: {
    accept: function (req) {
      if (!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      axios.post('/manager/requests', req, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => this.getRequests())
          .catch(r => console.log(r));
    },

    decline: function (req) {
      if (!localStorage.jws) {
        this.$router.push('/');
        return;
      }
      axios.put('/manager/requests', req, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
          .then(r => this.requests = r.data)
          .catch(r => console.log(r));
    },

  getRequests: function () {
    if (!localStorage.jws) {
      this.$router.push('/');
      return;
    }
    axios.get('/manager/requests', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
        .then(r => this.requests = r.data)
        .catch(r => console.log(r));
  },
 },
  mounted(){
    this.getRequests();
  },


}
</script>

<style scoped>

</style>
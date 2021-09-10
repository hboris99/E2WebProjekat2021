<template ref="navbar">
  <div id="navmain">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="#/home">Runeterra's Food Emporium</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0 ">
            <li class="nav-item">
              <router-link class="nav-link active"  to="/restaurants">Restaurants</router-link>
            </li>
            <div v-if="role === 'Admin'" class="navbar-nav me-auto mb-2 mb-lg-0 ">
              <li class="nav-item">
                <router-link class="nav-link active"  to="/admin">All users</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link active"  to="/admin/newuser">New user</router-link>
              </li>
              <li class="nav-item">
                <router-link class="nav-link active"  to="/admin/newrestaurant">New restaurant</router-link>
              </li>
            </div>

          </ul>

          <form class="d-flex">
            <input class="form-control me-2" type="search" placeholder="Search" :value="value" @input="onInput" aria-label="Search">
            <button class="btn btn-outline-success" type="submit" >Search</button>
          </form>

          <div v-if="!jws">
            <button class="btn btn-outline-success" type="submit" @click="$router.push('/register')">
              Sign Up
            </button>
            <button class="btn btn-outline-success" type="submit" @click="$router.push('/signin')">Sign in</button>
          </div>
          <div v-if="jws">
            <li class="list-unstyled nav-item dropdown" v-if="jws">
              <router-link class="nav-link dropdown-toggle" href="#" to="/userinfo" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                <i  class="far fa-user fa-2x"></i>
              </router-link>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                <li><router-link class="dropdown-item"  to="/profile">Profile</router-link></li>
                <li><router-link class="dropdown-item" v-if="role === 'Buyer'" to="/cart">Cart</router-link></li>
                <li><router-link class="dropdown-item" to="/orders">Orders </router-link></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#" @click="logout">Logout</a></li>
              </ul>
            </li>
          </div>
        </div>
      </div>
    </nav>

  </div>
</template>

<script>
module.exports=
    {

      data: function (){
        return{
          role: '',
          componentKey:0,
        }


      },
      props: {
        value: {
          type: [String],
          default: ''
        }
      },
      methods:{
        onInput: function (e){
          this.$emit('on-input', e.target.value)
        },
        getRole: function (){
          if(!localStorage.jws){
            return
          }
          axios.get('/user/role', {headers: {'Authorization': 'Bearer' + localStorage.jws}}).then(r => this.role = r.data);
        },
        logout: function () {
          localStorage.removeItem('jws');
          this.$router.go();
        }

      },
      computed:{

        jws: function (){
          return localStorage.jws;
        }
      },
      mounted(){
        this.getRole();
      },
    }

</script>

<style scoped>

</style>
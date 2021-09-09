<template>
  <div>
  <custom-nav-bar>

  </custom-nav-bar>
  <div id="mainview">
    <div id="newArticleForm">
    <form class="row g-3 needs-validation" @submit="newArticle">
      <div class="form-floating">
        <input class="form-control" id="articleNameForm" v-model="articleName">
        <label for="articleNameForm">Article name</label>
      </div>
      <div class="form-floating ">
        <input class="form-control" id="articlePriceForm" v-model="articlePrice">
        <label for="articlePriceForm">Article price</label>
      </div>
      <select clas="form-select form-select-lg" v-model="articleType">
        <option :value="null" disabled hidden>Select article type</option>
        <option value="Food">Food</option>
        <option value="Drink">Drink</option>
      </select>

      <div class="form-floating form-group">
        <input style="display: none" type="file" ref="file" @change="selectFile">
        <div v-if="!file" id="add-thumbnail" @click="$refs.file.click()">
          +
        </div>
        <img v-else :src="fileUrl" alt="rest img"/>

      </div>
      <div class="form-floating ">
        <input class="form-control" id="articleDescForm" v-model="articleDescription">
        <label for="articleDescForm">Article description</label>
      </div>
      <div class="form-floating ">
        <input class="form-control" id="articleQuantityForm" v-model="articleQuantity">
        <label for="articleQuantityForm">Article Quantity</label>
      </div>



      <input class="btn btn-primary" type="submit" value="Create new Article">

    </form>
  </div>
    </div>
  </div>
</template>

<script>
module.exports = {
  data: function (){
    return{
      articleName: '',
      articlePrice: '',
      articleType: '',
      file: '',
      fileUrl: '',
      articleDescription: '',
      articleQuantity: '',
    }
  },
  methods:{
    newArticle: function (){

      if(!localStorage.jws){
        this.$router.push('/');
      }
      let data = new FormData()
      let req = {
        name: this.articleName,
        price: this.price,
        articleType: this.articleType,
        quantity: this.articleQuantity,
        description: this.articleDescription,
      }
      data.append('req', JSON.stringify(req));
      data.append('file', this.file);
      let config = {
        headers:{
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer' + localStorage.jws,
        },
      };
      axios.post('/manager/restaurant/article ', data, config)
          .then(() => alert('Article added'))
          .catch(r => console.log(r));
    },
    selectFile: function (){
      let file = this.$refs.file.files[0]
      this.fileUrl = URL.createObjectURL(file);
      this.file = file;
    },
  }
}

</script>

<style scoped>
#mainview{
  display: grid;
  place-items: center;
  height: 100vh;
}
#newArticleForm{
  display: grid;
  place-items: center;
  padding: 20px;
  border: 1px solid rgba(103, 5, 2, 0.67);
  border-radius: 10px;
  width: 500px;
  height: 510px
}
</style>
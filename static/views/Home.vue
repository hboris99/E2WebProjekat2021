<template>
  <div>
      <custom-nav-bar >
      </custom-nav-bar>
    <router-view></router-view>

    <div id="top-view">
      <div id="container" class="container  pt-3">
        <div id="text-box" class="border-0   rounded shadow ">
          <div class="text-center mb-3 d-none d-lg-block">
            <h3 class="lead display-1 font-weight-bold">Welcome to <br>Runeterra's Food Emporium</h3>
            <h3>Explore the World</h3>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<style >
#top-view{
  min-height: 100vh;
  background:  url("../images/pozadinaFullHd.jpg") no-repeat center center;
}


#text-box{
 background-color: rgba(255,255,255,0.5);
  border-color: transparent;
  margin: 0;
}
#container{
  position: absolute;
  top: 35%;
  alignment: center;
  margin-left: 5%;
}
</style>

let update = false;
let name ="";
const cardDisplay = document.getElementById("product-container")
const dishId = getDishId();

/**
 * get dish id from URL
 */

function getDishId()
{
    const query = window.location.search.substring(1);
    const vars = query.split("&");
    for (let i=0; i<vars.length; i++) {
        const pair = vars[i].split("=");
        if(pair[0] == "dishId"){
            return pair[1];
        }
    }
    return false;
}

/**
 * submit form for PUT & POST
 */
document.getElementById("myiForm").addEventListener("submit",async function(event){
    event.preventDefault();
    let ingredient={};
    for(const elem of document.querySelectorAll("input")){
        ingredient[elem.name]=elem.value;
    }
        console.log(ingredient)
    if(update){
        await  updateIngredient(ingredient,name)
    }else {
        await addIngredient(ingredient);
    }

        for(const elem of document.querySelectorAll("input[type=text],input[type=number],input[type=text]")){
            elem.value="";

        }

        update = false;
        name ="";

})


async function deleteIngredient(event){
    var id = parseFloat(event.id);
    console.log("delete ID: "+id);
    await deleteIng(id);
}
/**
 * FetchAPI - GET(ingredients)
 */

async function getIngredients(){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/ingredients');
        // "http://localhost:8080/dishes/"+dishId+"/ingredients"
        console.log("Get Ingredients: "+response);
        const responseJson = await response.json();
        console.log("Get Ingredients Json: "+responseJson)
        generateIngredientHTML(responseJson)
    }catch (e) {
        alert("HTTP status: 400. Get ingredients error: "+e)
    }
}

getIngredients();

/**
 * FetchAPI - delete
 */
async function deleteIng(id){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/ingredients/'+id,{
            method: "DELETE"
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="500"){
                    alert("500, delete fail!");
                }else if(data.toString()==="200"){
                    location.reload();
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Delete ingredient error: "+e)
    }

}
/**
 * FetchAPI - POST
 */
async function addIngredient(ingredient){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/ingredients',{
            method: "POST",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(ingredient)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="500"){
                    alert("HTTP status: 500. Add ingredient error!");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myiForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Add ingredient error: "+e)
    }
}

/**
 * FetchAPI - PUT.
 */
async function updateIngredient(ingredient,name){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/ingredients/'+name,{

            method: "PUT",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(ingredient)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="400"){
                    alert("HTTP status: 400. Modify dish error! Invalid dish ID supplied");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myiForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Modify ingredient error: "+e)
    }
}

function openForm() {
    document.getElementById("myiForm").style.display = "block";
}

function closeForm() {
    document.getElementById("myiForm").style.display = "none";
}

function openCancel() {
    document.getElementById("cancelSearch").style.display = "block";
}

function closeCancel() {
    document.getElementById("cancelSearch").style.display = "none";
    location.reload();
}
function updateForm(event){
    document.getElementById("myiForm").style.display = "block";
    console.log(event.id)
    update = true;
    name = parseFloat(event.id);
    console.log("name:"+name);
}

/**
 * submit text for search.
 */
document.getElementById("searchBar").addEventListener("submit",async function(event){
    event.preventDefault();
    let search={};
    for(const elem of document.querySelectorAll("input[type=text]")){
        search[elem.name]=elem.value;
    }

    console.log(search);
    var searchBody = [];
    for (var property in search) {
        var encodedKey = encodeURIComponent(property);
        var encodedValue = encodeURIComponent(search[property]);
        searchBody.push(encodedKey + "=" + encodedValue);
    }
    searchBody = searchBody.join("&");

    console.log(searchBody);

    await searchFunction(searchBody);
    openCancel();
    for(const elem of document.querySelectorAll("input[type=text]")){
        elem.value="";
    }
})

/**
 * FetchAPI - Get (search).
 */
async function searchFunction(searchBody){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/ingredients/search?'+searchBody,{
            method: "GET",
            headers: {
                'Content-Type': 'Application/json'
            }
        }).then(res=>res.json())
            .then(data=>{
                document.getElementById("product-container").innerHTML=" "
                generateIngredientHTML(data)
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. search ingredient error: "+e)
    }

}

/**
 * create html card
 */
function generateIngredientHTML(responseJson){
    for(const item of responseJson){
        //create elements
        const weight = document.createElement("span");
        const name = document.createElement("h4");
        const description = document.createElement("p");
        const updateButton = document.createElement("button");
        const deleteButton = document.createElement("button");
        const cardBody = document.createElement("div");
        const card = document.createElement("div");
        const br = document.createElement("br")
        const br2 = document.createElement("br")


        //set class/id/onclick
        card.className="card";
        cardBody.className="card-body";
        weight.className = "tag tag-pink";
        description.className="card-des";
        updateButton.className="button button2";
        updateButton.type ="submit";
        updateButton.id = item.id+"a";
        updateButton.addEventListener("click",function (){updateForm(this)});
        deleteButton.className="button button3";
        deleteButton.id = item.id+"aa";
        deleteButton.addEventListener("click",function (){deleteIngredient(this)});

        //set innerHTML
        weight.innerHTML=item.weight+"g";
        name.innerHTML="Name: "+item.name;
        description.innerHTML="Description: "+item.description;
        updateButton.innerHTML="Modify";
        deleteButton.innerHTML="Delete";


        //build up
        cardBody.append(weight,br2,name,br,description,updateButton,deleteButton);
        card.append(cardBody);
        cardDisplay.append(card);
    }
}
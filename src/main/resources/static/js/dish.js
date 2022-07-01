let update = false;
let name ="";
const cardDisplay = document.getElementById("container")
/**
 * submit form for PUT & POST
 */
document.getElementById("myfForm").addEventListener("submit",async function(event){
    event.preventDefault();
    let dish={};
    for(const elem of document.querySelectorAll("input[type=text],input[type=text],input[type=number],input[type=text]")){
        dish[elem.name]=elem.value;
    }
    console.log(dish)
    if(update){
        await  updateFood(dish,name)
    }else {
        await  addFood(dish);
    }

    for(const elem of document.querySelectorAll("input[type=text],input[type=text],input[type=number],input[type=text]")){
        elem.value="";
    }
    update = false;
    name ="";

})


/**
 * FetchAPI - GET(Dishes)
 */
async function getDish(){
    try{
        const response = await fetch('http://localhost:8080/dishes');
        console.log("Get Dishes: "+response);
        const responseJson = await response.json();
        console.log("Get Dishes Json: "+responseJson)
        generateDishHTML(responseJson)
    }catch (e) {
        alert("HTTP status: 400. Get dish. Something went wrong : "+e)
    }
}

getDish();

/**
 * FetchAPI - POST
 */
async function addFood(dish){
    try{
        const response = await fetch('http://localhost:8080/dishes',{
            method: "POST",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(dish)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="500"){
                    alert("HTTP status: 500. Add dish error!");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myfForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Add dish. Something went wrong : "+e)
    }

}
/**
 * FetchAPI - PUT
 */
async function updateFood(dish,name){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+name,{
            method: "PUT",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(dish)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="404"){
                    alert("HTTP status: 404. Modify dish error!");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myfForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Modify dish. Something went wrong : "+e)
    }
}

/**
 * FetchAPI - DELETE
 */
async function deleteFood(id){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+id,{
            method: "DELETE"
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="500"){
                    alert("HTTP status: 500. Delete dish error!");
                }else if(data.toString()==="200"){
                    location.reload();
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Delete dish. Something went wrong : "+e)
    }

}

async function deleteDish(event){
    var id = parseFloat(event.id);
    console.log("delete ID: "+id);
    await deleteFood(id);
}

function updateForm(event){
    document.getElementById("myfForm").style.display = "block";
    update = true;
    name = parseFloat(event.id);
    console.log(name)
}

function jumpToIngredient(event){
    var id = parseFloat(event.id);
    const url = "ingredients.html?dishId=" + id;
    location.href=url;
}

function jumpToReview(event){
    var id = parseFloat(event.id);
    const url = "review.html?dishId=" + id;
    location.href=url;
}

function openForm() {
    document.getElementById("myfForm").style.display = "block";
}

function closeForm() {
    document.getElementById("myfForm").style.display = "none";
}

function openCancel() {
    document.getElementById("cancelSearch").style.display = "block";
}

function closeCancel() {
    document.getElementById("cancelSearch").style.display = "none";
    location.reload();
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
        const response = await fetch('http://localhost:8080/dishes/search?'+searchBody,{
            method: "GET",
            headers: {
                'Content-Type': 'Application/json'
            }
        }).then(res=>res.json())
            .then(data=>{
                console.log(data)
                document.getElementById("container").innerHTML=" "
               generateDishHTML(data)
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Search dish. Something went wrong : "+e)
    }

}
/**
 * create html card
 */
function generateDishHTML(responseJson){
    for(const item of responseJson){
        //create elements
        const card =document.createElement("div")
        const name =document.createElement("h3")
        const category =document.createElement("h3")
        const time =document.createElement("h3")
        const content1 = document.createElement("div")
        const face1 = document.createElement("div")

        const description =document.createElement("p")
        const deleteButton =document.createElement("a")
        const updateButton =document.createElement("a")
        const ingredientButton =document.createElement("a")
        const reviewButton =document.createElement("a")
        const content2 = document.createElement("div")
        const face2 = document.createElement("div")

        //set class/id/onclick
        deleteButton.id= item.id+"dd"
        deleteButton.addEventListener("click",function (){deleteDish(this)})
        updateButton.id = item.id+"d"
        updateButton.addEventListener("click",function (){updateForm(this)})
        updateButton.type="submit"
        ingredientButton.id=item.id+"ddd"
        ingredientButton.addEventListener("click",function (){jumpToIngredient(this)})
        reviewButton.id=item.id+"dddd"
        reviewButton.addEventListener("click",function (){jumpToReview(this)})
        content2.className="content"
        content1.className="content"
        face2.className="face face2"
        face1.className="face face1"
        card.className="card"

        //set innerHTML
        name.innerHTML ="Name: "+item.name
        category.innerHTML="Category: "+item.category
        time.innerHTML="Time: "+item.time
        description.innerHTML="Description: "+item.description
        updateButton.innerHTML="Modify"
        deleteButton.innerHTML="Delete"
        ingredientButton.innerHTML = "View ingredient list"
        reviewButton.innerHTML = "Review"

        //build up
        content1.append(name,category,time);
        face1.append(content1);
        content2.append(description,deleteButton,updateButton,reviewButton,ingredientButton)
        face2.append(content2)
        card.append(face1,face2)
        cardDisplay.append(card)
    }

}
let update = false;
let name ="";
const cardDisplay = document.getElementById("cards")
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
document.getElementById("myrForm").addEventListener("submit",async function(event){
    event.preventDefault();
    let review={};
    for(const elem of document.querySelectorAll("input[type=number],input[type=text]")){
        review[elem.name]=elem.value;
    }
    console.log(review)
    if(update){
        await  updateReview(review,name)
    }else {
        await  addReview(review);
    }

    for(const elem of document.querySelectorAll("input[type=number],input[type=text]")){
        elem.value="";
    }
    update = false;
    name ="";

})

/**
 * FetchAPI - GET(Reviews)
 */
async function getReview(){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/reviews');
        console.log("Get reviews: "+response);
        const responseJson = await response.json();
        console.log("Get reviews Json: "+responseJson)
        generateReviewHTML(responseJson)
    }catch (e) {
        alert("HTTP status: 400. Get review error: "+e)
    }
}

getReview();

/**
 * FetchAPI - POST
 */
async function addReview(review){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/reviews',{
            method: "POST",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(review)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="500"){
                    alert("HTTP status: 500. Add dish error!");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myrForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Add review error: "+e)
    }

}

/**
 * FetchAPI - PUT
 */
async function updateReview(review,name){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/reviews/'+name,{
            method: "PUT",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(review)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="400"){
                    alert("HTTP status: 400. Modify dish error! Invalid dish ID supplied");
                }else if(data.toString()==="201"){
                    location.reload();
                    document.getElementById("myrForm").style.display = "none";
                }
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Modify review error: "+e)
    }
}
/**
 * FetchAPI - DELETE
 */
async function deleteReview(id){
    try{
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/reviews/'+id,{
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
        alert("HTTP status: 400. Delete review error: "+e)
    }

}

async function deleteReviewID(event){
    var id = parseFloat(event.id);
    console.log("delete ID: "+id);
    await deleteReview(id);
}

function updateForm(event){
    document.getElementById("myrForm").style.display = "block";
    update = true;
    name = parseFloat(event.id);
    console.log(name)
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
        const response = await fetch('http://localhost:8080/dishes/'+dishId+'/reviews/search?'+searchBody,{
            method: "GET",
            headers: {
                'Content-Type': 'Application/json'
            }
        }).then(res=>res.json())
            .then(data=>{
                console.log(data)
                document.getElementById("cards").innerHTML=" "
                generateReviewHTML(data)
            })
    }catch (e){
        console.error(e);
        alert("HTTP status: 400. Search review error: "+e)
    }

}

/**
 * create html card
 */
function generateReviewHTML(responseJson){
    for(const item of responseJson){
        //create elements
        const card =document.createElement("div")
        const  modifyButton  = document.createElement("a")
        const  deleteButton = document.createElement("a")
        const  card_apply = document.createElement("p")
        const description = document.createElement("h2")
        const score = document.createElement("div")



        //set class/id/onclick
        card.className = "card card-4"
        score.className="card__icon"
        description.className ="card__title"
        card_apply.className = "card__apply"
        deleteButton.className = "card__link__d"
        modifyButton.className = "card__link"
        deleteButton.id=item.id+"rr"
        modifyButton.id=item.id+"r"
        deleteButton.addEventListener("click",function (){deleteReviewID(this)})
        modifyButton.addEventListener("click",function(){updateForm(this)})


        //set innerHTML
        deleteButton.innerHTML="Delete"
        modifyButton.innerHTML="Modify"
        description.innerHTML="Description: "+item.description
        score.innerHTML="Score: "+item.score


        //build up
        card_apply.append(modifyButton,deleteButton)
        card.append(score,description,card_apply)
        cardDisplay.append(card)
    }

}



function openForm() {
    document.getElementById("myrForm").style.display = "block";
}

function closeForm() {
    document.getElementById("myrForm").style.display = "none";
}
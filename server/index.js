'use strict'

let express = require('express')
let app = express()

let shortid = require('shortid')

if(global.flags === undefined) {
  global.flags = [{id:shortid.generate(), lat:"47.2591047", lng:"8.58588499", team: "blue", capture: 5},
                  {id:shortid.generate(), lat:"47.259882", lng:"8.585152", team: "red", capture: 5},
                  {id:shortid.generate(), lat:"47.259882", lng:"8.585731", team: "blue", capture: 20},
                  {id:shortid.generate(), lat:"47.258783", lng:"8.5860585", team: "red", capture: 100},
                  {id:shortid.generate(), lat:"47.25897703", lng:"8.58528803", team: "red", capture: 0}]
}

if(global.players === undefined) {
  global.players = []
}

app.get('/reset', function (req, res) {
	
	global.flags = [{id:shortid.generate(), lat:"47.2591047", lng:"8.58588499", team: "blue", capture: 5},
                    {id:shortid.generate(), lat:"47.259882", lng:"8.585152", team: "red", capture: 5},
                    {id:shortid.generate(), lat:"47.259882", lng:"8.585731", team: "blue", capture: 20},
                    {id:shortid.generate(), lat:"47.258783", lng:"8.5860585", team: "red", capture: 100},
                    {id:shortid.generate(), lat:"47.25897703", lng:"8.58528803", team: "red", capture: 0}]
				  
	global.players = []
				  
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify({"status":"reset"}))

})


app.get('/flags/list', function (req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(global.flags))

})

app.get('/flags/capture', function (req, res) {
    let id = req.query.id
    let team = req.query.team

    if(!id) return res.status(400).send({ error: 'Nope' });

    console.log(id, " ", team)

    if(!team || team.length >= 1)  {

    for(let i = 0; i < global.flags.length; i++) {
      if(global.flags[i].id === id) {
        if(global.flags[i].capture <= 0) {
          global.flags[i].capture += 5
          global.flags[i].team = team
        } else {
          if(global.flags[i].team == team) {
            if(global.flags[i].capture < 100) {
              global.flags[i].capture += 5;
            }
          } else {
            if(global.flags[i].capture > 0) {
              global.flags[i].capture -= 5;
            }
          }
        }

        res.setHeader('Content-Type', 'application/json')
        res.send(global.flags[i])

        return
      }
    }

  }
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(global.flags))
})

app.get('/players/list', function (req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(global.players))
})

app.get('/players/update', function (req, res) {

    let id = req.query.id || shortid.generate()
    let lat = req.query.lat
    let lng = req.query.lng
    let team = req.query.team


    if(!lat || !lng) return res.status(400).send({ error: 'Nope' });

    console.log("id",id,"lng",lng,"lat",lat)
    for(let i = 0; i < global.players.length; i++) {
      if(global.players[i].id === id) {
        global.players[i].lat = lat
        global.players[i].lng = lng
        global.players[i].team = team

        res.setHeader('Content-Type', 'application/json')
        res.send(global.players[i])

        return
      }
    }

    let newPlayer = {id,lat,lng,team}
    global.players.push(newPlayer)

    res.setHeader('Content-Type', 'application/json')
    res.send(newPlayer)

})

app.get('/players/delete', function (req, res) {
    let id = req.query.id

    if(!id) return res.status(400).send({ error: 'Nope' });

    for(let player of global.players) {
      if(player.id === id) {
        global.players.splice(global.players.indexOf(player), 1)
        break
      }
    }


    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(global.players))
})

app.listen(2016, function () {
  console.log('ROLO listening on 2016')
})

const express = require('express');

const router = express.Router();

module.exports = router;

const server = require('./server');

router.get('/', (req, res) => {
    res.send('URI:' + app.uri);
});

router.get('/list', async (req, res) => {
    await server.mongoose.connect(server.uri);

    let sanphams = await server.spModel.find();

    console.log(sanphams);

    res.send(sanphams);
});

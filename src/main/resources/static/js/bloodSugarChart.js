var bloodSugars = /*[[${bloodSugars}]]*/ [];

var sugarLevels = bloodSugars.map(function (bloodSugar) {
    return bloodSugar.sugar;
});

var times = bloodSugars.map(function (bloodSugar) {
    return bloodSugar.dateTime;
});

var formattedTimes = times.map(function (time) {
    return moment(time).format('HH:mm DD.MM.YYYY');
});

var ctx = document.getElementById('myChart').getContext('2d');
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: formattedTimes,
        datasets: [{
            label: 'Уровень глюкозы',
            data: sugarLevels,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            x: {
                display: true,
                title: {
                    display: true,
                    text: 'Дата и время'
                }
            },
            y: {
                display: true,
                title: {
                    display: true,
                    text: 'Уровень глюкозы'
                }
            }
        }
    }
});
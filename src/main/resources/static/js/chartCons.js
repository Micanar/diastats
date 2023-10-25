var consumptions = /*[[${consumptions}]]*/ [];
var easy = 0;
var hard = 0;


// Подсчет количества показаний в каждой категории
consumptions.forEach(function (consumption) {
    if (consumption.carbohydrateType == "Простой") {
        easy=easy+consumption.grams;
    } else if (consumption.carbohydrateType == "Сложный") {
        hard=hard+consumption.grams;
    }
});

// Создание круговой диаграммы с использованием Chart.js
var ctx = document.getElementById('myChart').getContext('2d');
var myChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ['Простые углеводы', 'Сложные углеводы'],
        datasets: [{
            data: [easy, hard],
            backgroundColor: [ 'rgba(255,0,0,0.46)', 'rgba(80,255,0,0.46)']
        }]
    },
    options: {
        responsive: true
    }
});
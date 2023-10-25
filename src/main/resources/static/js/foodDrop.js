(function(){
    var counter = document.querySelector('.counter');
    var diner = document.querySelector('h1');
    var menu = ['🍔', '🌭', '🍕', '🍗', '🌮', '🍔'];
    var noms = ['Углеводы', 'Белки', 'Жиры', 'Каллории', 'Хлебные единицы'];
    var finished = 0;

    function jelly(e) {
        diner.removeEventListener('fit', jelly);
        diner.animate(
            [
                { transform: 'scale(' + 1 / e.detail.scaleFactor + ')' },
                { transform: 'scale(1.1)' },
                { transform: 'scale(.9)' },
                { transform: 'scale(1.05)' },
                { transform: 'scale(.98)' },
                { transform: 'scale(1)' }
            ],
            {
                duration:500
            }
        );
    }

    // تاثيرات الطعام
    function nom(index) {

        var nommer = document.getElementById('nommer-' + index);
        var rotation = -20 + (Math.random() * 40);
        var scale = .75 + (Math.random() * .5);

        nommer.animate(
            [
                { opacity:0, transform: 'scale(' + .25 * scale + ') rotateZ(' + rotation + 'deg) translateY(0) ' },
                { opacity:1, transform: 'scale(' + scale + ') rotateZ(' + rotation + 'deg) translateY(-.5em) ' }
            ],
            {
                duration:250
            }
        );

        diner.animate(
            [
                { transform: 'scaleY(1)' },
                { transform: 'scaleY(' + (.7 + (Math.random() * .2)) + ')' },
                { transform: 'scaleY(1)' }
            ],
            {
                duration:100
            }
        );
    }

    // خاص بدعم التاثير المتحرك
    if ('animate' in diner) {
        diner.addEventListener('fit', jelly);
    }

    var dishes = menu
        .concat(menu) // duplicate icons
        .sort(function() { return .5 - Math.random(); })
        .concat(['🐝']) // unfortunately there's no fly emoji
        .map(function(menuItem, i) {

            var offset = 33.33 + (Math.random() * 33.33);

            var dish = document.createElement('div');
            dish.textContent = menuItem;
            dish.setAttribute('aria-hidden', 'true');
            dish.setAttribute('data-nommer', i);
            dish.className = 'dish';
            dish.style.cssText = 'left:' + offset + '%; transition-delay: ' + ((i * 200) + Math.random() * 100) + 'ms';

            var nommer = document.createElement('div');
            nommer.id = 'nommer-' + i;
            nommer.textContent = noms[Math.floor(Math.random() * noms.length)];
            nommer.setAttribute('aria-hidden', 'true');
            nommer.className = 'nommer';
            nommer.style.cssText = 'left:' + offset + '%; opacity:0;';

            counter.appendChild(dish);
            counter.appendChild(nommer);

            return dish;
        });

    setTimeout(function() {
        requestAnimationFrame(function() {
            dishes.forEach(function (dish) {
                dish.classList.add('drop');
                dish.addEventListener('transitionend', burb);
            })
        });
    }, 500);

    function burb(e) {
        e.target.removeEventListener('transitionend', burb);
        e.target.style.opacity = 0;

        finished++;
        if (finished === dishes.length) {
            full();
        }
        else if ('animate' in diner) {
            nom(e.target.getAttribute('data-nommer'));
        }
    }

    // أنتهي!
    function full() {
        fitty(diner, {
            minSize:64
        });
    }
}());
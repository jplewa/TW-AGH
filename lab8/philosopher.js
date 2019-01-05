var Fork = function () {
    this.state = 0;
    return this;
}

// zaimplementuj funkcje acquire, tak by korzystala z algorytmu BEB
// (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
// 1. przed pierwsza proba podniesienia widelca Filozof odczekuje 1ms
// 2. gdy proba jest nieudana, zwieksza czas oczekiwania dwukrotnie
//    i ponawia probe itd.

Fork.prototype.acquire = function (id, cb) {
    var counter = 1;
    var fork = this;
    var beb = function() {
        if (fork.state == 0) {
            console.log("<" + id + "> fork taken [" + counter + "]");
            //countSum = 0
            //for (var i = 1; i <= counter; i *= 2) {
            //    countSum += i;
            //}
            //console.log(id + "," + countSum)
            fork.state = 1;
            counter = 1;
            if (cb) cb();
        } else {
            counter *= 2;
            setTimeout(beb, counter);
        }
    }
    setTimeout(beb, counter);
}

Fork.prototype.release = function() {
    this.state = 0;
}

var Conductor = function (limit) {
    this.state = 0;
    this.limit = limit;
    return this;
}

Conductor.prototype.acquire = function (id, cb) {
    var counter = 1;
    var beb = function () {
        if (this.state < this.limit) {
            console.log("<" + id + "> fork taken [" + counter + "]")
            //countSum = 0
            //for (var i = 1; i <= counter; i *= 2) {
            //    countSum += i;
            //}
            //console.log(id + "," + countSum)
            counter = 1;
            this.state++;
            if (cb) cb();
        } else {
            counter *= 2;
            setTimeout(beb.bind(this), counter);
        }
    }
    setTimeout(beb.bind(this), counter);
}

Conductor.prototype.release = function () {
    this.state--;
}

var Philosopher = function (id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id + 1) % forks.length;
    return this;
}

// zaimplementuj rozwiazanie naiwne
// kazdy filozof powinien 'count' razy wykonywac cykl
// podnoszenia widelcow -- jedzenia -- zwalniania widelcow
Philosopher.prototype.startNaive = function (count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    for (var j = 0; j < count; j++) {
        forks[f1].acquire(id, function () {
            forks[f2].acquire(id, function () {
                console.log("<" + id + "> meal starting"),
                forks[f1].release();
                forks[f2].release();
                console.log(id + " meal finished");
            })
        });
    }
}

// zaimplementuj rozwiazanie asymetryczne
// kazdy filozof powinien 'count' razy wykonywac cykl
// podnoszenia widelcow -- jedzenia -- zwalniania widelcow
Philosopher.prototype.startAsym = function (count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    
    var phil = this;

    var first = id % 2 == 0 ? f2 : f1;
    var second = id % 2 == 0 ? f1 : f2;

    if (count > 0) {
        forks[first].acquire(id, function() {
            forks[second].acquire(id, function() {
                console.log("<" + id + "> meal starting");
                forks[first].release();
                forks[second].release();
                console.log("<" + id + "> meal finished");
                phil.startAsym(count - 1);
            });
        });
    }
}

// zaimplementuj rozwiazanie z kelnerem
// kazdy filozof powinien 'count' razy wykonywac cykl
// podnoszenia widelcow -- jedzenia -- zwalniania widelcow
Philosopher.prototype.startConductor = function (count, conductor) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    if (count > 0) {
        conductor.acquire(id, function () {
            forks[f1].acquire(id, function () {
                forks[f2].acquire(id, function () {
                    //console.log("<" + id + "> meal starting"),
                    forks[f1].release();
                    forks[f2].release();
                    //console.log("<" + id + "> meal finished");
                    conductor.release();
                    () => this.startConductor(count - 1, conductor);
                })
            });
        });
    }
}


var N = 5;
var forks = [];
var philosophers = []
var conductor = new Conductor(N - 1)
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}


for (var i = 0; i < N; i++) {
  //philosophers[i].startNaive(10);
}



for (var i = 0; i < N; i++) {
    philosophers[i].startAsym(10);
}




for (var i = 0; i < N; i++) {
    //philosophers[i].startConductor(10, conductor);
}



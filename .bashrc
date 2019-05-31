# laravel new-app
alias laravel="git clone -o laravel -b develop https://github.com/laravel/laravel.git"

alias art="php artisan"
alias art:mig="php artisan migrate"
alias art:reset="php artisan migrate:reset && php artisan migrate --seed"
alias art:ref="php artisan migrate:refresh --seed"
alias art:roll="php artisan migrate:rollback"
alias t="vendor/bin/phpunit --debug"
alias art:sch="php artisan schedule:run"
alias art:cmpl="php artisan clear-compiled"
alias seed="php artisan make:seeder"
alias ejeseed="php artisan db:seed --class="
alias art:mig:seed "php artisan migrate --seed"

alias art:jobs="art queue:work --sleep=3 --tries=3 --daemon"

alias art:dump="php artisan dump autoload"
alias art:cac="php artisan cache:clear"
alias art:vc="php artisan view:clear"
alias art:rc="php artisan route:clear"
alias art:opt="php artisan optimize"
alias art:cls="art clear-compiled && art cache:clear && art view:clear && art route:clear && art optimize && art debugbar:clear"


# Git
alias ga="git add"
alias gaa="git add ."
alias gc="git commit -m"
alias gps="git push"
alias gp="git pull"
alias gs="git status"
alias gl="git log --stat=100 --stat-graph-width=12"
alias gdiscard="git clean -df && git checkout -- ."
alias setpass="git config --global credential.helper wincred"

#Console
alias cls="clear"

#Composer
alias comp:up="composer.phar self-update"
alias comp:dump="composer dump-autoload"
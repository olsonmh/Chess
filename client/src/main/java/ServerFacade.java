public class ServerFacade {

    public void register(){}

    public void login(){}

    public void logout(){}

    public void createGame(){}

    public void joinGame(){}

    public void listGames(){}

    public void clearAll(){}


    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    /*
    @PostMapping("/register")
    public void register(@RequestBody User user) {
        gameService.register(user);
    }

    @PostMapping("/login")
    public void login(@RequestBody User credentials) {
        gameService.login(credentials);
    }

    @PostMapping("/logout")
    public void logout() {
        gameService.logout();
    }

    @PostMapping("/createGame")
    public void createGame(@RequestBody Game game) {
        gameService.createGame(game);
    }

    @PostMapping("/joinGame")
    public void joinGame(@RequestParam String gameId, @RequestBody User player) {
        gameService.joinGame(gameId, player);
    }

    @GetMapping("/listGames")
    public List<Game> listGames() {
        return gameService.listGames();
    }

    @DeleteMapping("/clearAll")
    public void clearAll() {
        gameService.clearAll();
    }
    */
}

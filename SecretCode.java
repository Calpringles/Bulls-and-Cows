
abstract class SecretCode {
    abstract boolean validateGuess(String guess);
    abstract int[] calcCowsAndBulls(String guess);
    abstract int getLength();
    abstract String getSecret();
    abstract void setSecret(String secret);
}
public enum EnemyType
{
    BASIC(10),
    ADVANCED(20),
    STEALTH(30),
    TANK(50),
    CIVILIAN(-100);

    int points;

    EnemyType(int points)
    { this.points = points; }
}
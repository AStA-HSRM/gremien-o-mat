#! /usr/bin/env python3

# IDEEN:
# DJANGO für Weboberfläche
# MySQL für Datenbank

import sys
from src import envhandler
from src import utility

if __name__ == "__main__":
    try:
        pass

    except KeyboardInterrupt as e:
        print("\n[{}] [OK] [{}] {}".format(utility.getTime(), type(e).__name__, e))
        sys.exit(0)
    except Exception as e:
        print("\n[{}] [ERROR] [{}] {}".format(utility.getTime(), type(e).__name__, e))
    finally:
        print("Program stopped")
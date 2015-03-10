package drzhark.mocreatures.utils;

import org.apache.logging.log4j.LogManager;

public class MoCLog {

    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger("MoCreatures");

    public static void initLog() {
        logger.info("Starting MoCreatures 6.3.0.d2");
        logger.info("Copyright (c) DrZhark, Bloodshot, BlockDaddy 2013");
    }
}

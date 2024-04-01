package org.embulk.output.databricks;

import java.util.Properties;
import org.embulk.EmbulkSystemProperties;
import org.embulk.input.file.LocalFileInputPlugin;
import org.embulk.output.DatabricksOutputPlugin;
import org.embulk.output.databricks.util.ConfigUtil;
import org.embulk.output.databricks.util.ConnectionUtil;
import org.embulk.parser.csv.CsvParserPlugin;
import org.embulk.spi.FileInputPlugin;
import org.embulk.spi.OutputPlugin;
import org.embulk.spi.ParserPlugin;
import org.embulk.test.TestingEmbulk;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public abstract class AbstractTestDatabricksOutputPlugin {
  private static final EmbulkSystemProperties EMBULK_SYSTEM_PROPERTIES =
      EmbulkSystemProperties.of(new Properties());

  @Rule public TemporaryFolder testFolder = new TemporaryFolder();

  @Before
  public void setup() {
    if (ConfigUtil.disableOnlineTest()) {
      return;
    }
    ConnectionUtil.dropAllTemporaryTables();
  }

  @After
  public void cleanup() {
    if (ConfigUtil.disableOnlineTest()) {
      return;
    }
    ConnectionUtil.dropAllTemporaryTables();
  }

  @Rule
  public TestingEmbulk embulk =
      TestingEmbulk.builder()
          .setEmbulkSystemProperties(EMBULK_SYSTEM_PROPERTIES)
          .registerPlugin(FileInputPlugin.class, "file", LocalFileInputPlugin.class)
          .registerPlugin(ParserPlugin.class, "csv", CsvParserPlugin.class)
          .registerPlugin(OutputPlugin.class, "databricks", DatabricksOutputPlugin.class)
          .build();
}

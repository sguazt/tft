/* vim: set tabstop=4 expandtab shiftwidth=4 softtabstop=4: */

/**
 * \file epoch2date.cpp
 *
 * \brief Convert a UNIX epoch into a text string.
 *
 * \author Marco Guazzone (marco.guazzone@gmail.com)
 *
 * <hr/>
 *
 * Copyright 2017 Marco Guazzone (marco.guazzone@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <ctime>
#include <cstdlib>
#include <sstream>
#include <iostream>


int main(int argc, char *argv[])
{
	std::time_t ts;

	if (argc > 1)
	{
		std::istringstream iss(argv[1]);
		iss >> ts;
	}
	else
	{
		// Current time as UNIX epochs
 		ts = time(NULL);
	}

	std::cout << "UNIX epoch: " << ts << std::endl;
	std::cout << "-> ctime: " << std::ctime(&ts);
	std::cout << "-> gmtime+asctime: " << std::asctime(std::gmtime(&ts));
	std::cout << "-> locatime+asctime: " << std::asctime(std::localtime(&ts));
}

/**
 * Copyright (C) 2013 the original author or authors.
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

package controllers;

import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.session.Session;

import com.google.inject.Inject;

public class ApplicationController {
  
  // Context is always per request. Per request should always be inside the request method.
  // If you'd use Context here it would assume that context is the same for the whole application
  @Inject
  Context injectedContext;

  // Yes! That's correct... Context is now specific to the http request coming in to this method.
  public Result index(Context context) {
    String foo = "";

    if (context.getSession() != null && context.getSession().get("foo") != null) {
      foo = context.getSession().get("foo");
    }

    return Results.html().render("foo", foo);
  }

  // Also use the context inside the method... then it'll work..
  // Something like  public Result storeDi(Context context, Session session) {...}
  // (Yes - you can also use the session inside the method spec...)
  public Result storeDi() {
    // won't work as injectedContext is not specific to this request...
    Session session = injectedContext.getSession();
    session.put("foo", "bar");

    Result result = Results.redirect("/");

    // I would have to do this
    session.save(injectedContext, result);

    return result;
  }

  // That again should work perfectly fine...
  public Result storeNonDi(Session session) {
    session.put("foo", "bar");

    Result result = Results.redirect("/");
    return result;
  }

  // All good.
  public Result clearSession(Session session) {
    session.clear();

    Result result = Results.redirect("/");
    return result;
  }
}
